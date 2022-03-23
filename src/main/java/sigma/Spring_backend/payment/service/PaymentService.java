package sigma.Spring_backend.payment.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;
import sigma.Spring_backend.payment.dto.*;
import sigma.Spring_backend.payment.entity.Payment;
import sigma.Spring_backend.payment.entity.PaymentWebhook;
import sigma.Spring_backend.payment.repository.PaymentRepository;
import sigma.Spring_backend.payment.repository.PaymentWebhookRepository;
import sigma.Spring_backend.reservation.entity.Reservation;
import sigma.Spring_backend.reservation.repository.ReservationRepo;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {

	private final PaymentRepository paymentRepository;
	private final MemberRepository memberRepository;
	private final PaymentWebhookRepository webhookRepository;
	private final ReservationRepo reservationRepo;

	@Value("${payments.toss.test_client_api_key}")
	private String testClientApiKey;

	@Value("${payments.toss.test_secret_api_key}")
	private String testSecretApiKey;

	@Value("${payments.toss.live_client_api_key}")
	private String liveClientApiKey;

	@Value("${payments.toss.live_secret_api_key}")
	private String liveSecretApiKey;

	@Value("${payments.toss.success_url}")
	private String successCallBackUrl;

	@Value("${payments.toss.fail_url}")
	private String failCallBackUrl;

	@Value("${payments.toss.origin_url}")
	private String tossOriginUrl;

	@Transactional
	public PaymentRes requestPayments(PaymentReq paymentReq) {
		Long amount = paymentReq.getAmount();
		String payType = paymentReq.getPayType().getName();
		String customerEmail = paymentReq.getCustomerEmail();
		String orderName = paymentReq.getOrderName().name();
		Long reservationSeq = paymentReq.getReservationSeq();

		Reservation reservation = reservationRepo.findById(reservationSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND));
		if (reservation.getPayYn().equals("Y"))
			throw new BussinessException(ExMessage.RESERVATION_ERROR_ALREADY_PAY);

		if (reservation.getCancelYn().equals("Y"))
			throw new BussinessException(ExMessage.RESERVATION_ERROR_ALREADY_CANCEL);

		if (paymentRepository.findByReservationSeq(reservationSeq).isPresent()) {
			throw new BussinessException("이미 결제 신청된 예약입니다. 결제를 완료해주세요.");
		}

		if (amount == null || amount != 3000) {
			throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_PRICE);
		}

		if (!payType.equals("가상계좌") && !payType.equals("카드")) {
			throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_PAY_TYPE);
		}

		if (!orderName.equals(ORDER_NAME_TYPE.CRDI_OR_PRODUCT_RECMD.name()) &&
				!orderName.equals(ORDER_NAME_TYPE.STYLE_FEEDBACK.name())) {
			throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NAME);
		}

		PaymentRes paymentRes;
		try {
			Payment payment = paymentReq.toEntity();
			memberRepository.findByEmailFJ(customerEmail)
					.ifPresentOrElse(
							M -> M.addPayment(payment)
							, () -> {
								throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);
							});
			paymentRes = payment.toRes();
			paymentRes.setSuccessUrl(successCallBackUrl);
			paymentRes.setFailUrl(failCallBackUrl);
			if (payment.getPayType().equals(PAY_TYPE.VIRTUAL_ACCOUNT)) {
				paymentRes.setValidHours(6);
				paymentRes.setCashReceiptType("소득공제");
				paymentRes.setUseEscrow(false);
			}
			return paymentRes;
		} catch (Exception e) {
			throw new BussinessException(ExMessage.DB_ERROR_SAVE);
		}
	}

	@Transactional
	public void verifyRequest(String paymentKey, String orderId, Long amount) {
		paymentRepository.findByOrderId(orderId)
				.ifPresentOrElse(
						P -> {
							// 가격 비교
							if (P.getAmount().equals(amount)) {
								P.setPaymentKey(paymentKey);
							} else {
								throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_AMOUNT);
							}
						}, () -> {
							throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER);
						}
				);
	}

	@Transactional
	public PaymentResHandleDto requestFinalPayment(String paymentKey, String orderId, Long amount) {
		Payment pay = paymentRepository.findByPaymentKey(paymentKey)
				.orElseThrow(() -> new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND));
		PAY_TYPE payType = pay.getPayType();
		Long reservationSeq = pay.getReservationSeq();
		Reservation reservation = reservationRepo.findById(reservationSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND));

		RestTemplate rest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		String encodedAuth = new String(Base64.getEncoder().encode((testSecretApiKey + ":").getBytes(StandardCharsets.UTF_8)));
		headers.setBasicAuth(encodedAuth);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		JSONObject param = new JSONObject();
		param.put("orderId", orderId);
		param.put("amount", amount);

		PaymentResHandleDto payResDto;
		try {
			payResDto = rest.postForEntity(
					tossOriginUrl + paymentKey,
					new HttpEntity<>(param, headers),
					PaymentResHandleDto.class
			).getBody();
			if (payResDto == null) throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER);
		} catch (Exception e) {
			String errorResponse = e.getMessage().split(": ")[1];
			String errorMessage = new Gson()
					.fromJson(
							errorResponse.substring(1, errorResponse.length() - 1),
							TossErrorDto.class
					).getMessage();
			throw new BussinessException(errorMessage);
		}

		if (payType.equals(PAY_TYPE.CARD)) {
			PaymentResHandleCardDto card = payResDto.getCard();
			paymentRepository.findByOrderId(payResDto.getOrderId())
					.ifPresent(payment -> {
						payment.setCardCompany(card.getCompany());
						payment.setCardNumber(card.getNumber());
						payment.setCardReceiptUrl(card.getReceiptUrl());
						payment.setPaySuccessYn("Y");
						reservation.setPayYn("Y");
					});
		} else if (payType.equals(PAY_TYPE.VIRTUAL_ACCOUNT)) {
			PaymentResHandleVirtualDto virtualAccount = payResDto.getVirtualAccount();
			paymentRepository.findByOrderId(payResDto.getOrderId())
					.ifPresent(payment -> {
						payment.setVirtualAccountNumber(virtualAccount.getAccountNumber());
						payment.setVirtualBank(virtualAccount.getBank());
						payment.setVirtualDueDate(virtualAccount.getDueDate());
						payment.setVirtualRefundStatus(virtualAccount.getRefundStatus());
						payment.setVirtualSecret(payResDto.getSecret());
						payment.setPaySuccessYn("N");
					});
		}

		return payResDto;
	}

	@Transactional
	public PaymentResHandleFailDto requestFail(String errorCode, String errorMsg, String orderId) {
		Payment payment = paymentRepository.findByOrderId(orderId)
				.orElseThrow(() -> new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND));
		payment.setPaySuccessYn("N");
		payment.setPayFailReason(errorMsg);

		return PaymentResHandleFailDto
				.builder()
				.orderId(orderId)
				.errorCode(errorCode)
				.errorMsg(errorMsg)
				.build();
	}

	@Transactional(readOnly = true)
	public List<PaymentDto> getAllPayments(Long memberSeq, PageRequest pageRequest) {
		String email = memberRepository.findBySeqFJ(memberSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND))
				.getEmail();

		return paymentRepository.findAllByCustomerEmail(email, pageRequest)
				.stream().map(Payment::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public void handleVirtualAccountIncome(TossVirtualDto tossVirtualDto) {
		String status = tossVirtualDto.getStatus();
		String orderId = tossVirtualDto.getOrderId();
		String secret = tossVirtualDto.getSecret();

		Payment payment = paymentRepository.findByOrderId(orderId)
				.orElseThrow(() -> new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND));
		Long reservationSeq = payment.getReservationSeq();
		Reservation reservation = reservationRepo.findById(reservationSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND));


		if (status.equals("DONE")) {
			log.info("입금 확인");
			payment.getCustomer().getPayments()
					.stream()
					.filter(p -> p.getOrderId().equals(orderId))
					.filter(p -> p.getVirtualSecret().equals(secret))
					.findFirst()
					.ifPresentOrElse(P -> {
						log.info("결제 성공 체크");
						P.setPaySuccessYn("Y");
						reservation.setPayYn("Y");
					}, () -> {
						throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND);
					});
		} else if (status.equals("CANCELED")) {
			log.info("입금 취소");
			payment.getCustomer().getPayments()
					.stream()
					.filter(p -> p.getOrderId().equals(orderId))
					.findFirst()
					.ifPresentOrElse(P -> {
						log.info("결제 취소 체크");
						if (P.getPaySuccessYn().equals("Y")) {
							log.info("결제 취소 체크 때 결제 완료라고 되있으면 롤백");
							P.setPaySuccessYn("N");
						}
						P.setCancelYn("Y");
						reservation.setPayYn("N");
						reservation.setCancelYn("Y");
					}, () -> {
						throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND);
					});
		}
	}

	@Transactional
	public void registTossPaymentWebhook(TossWebhookDto webhookDto) {
		String paymentKey = webhookDto.getData().getPaymentKey();
		Long seq = paymentRepository.findByPaymentKey(paymentKey)
				.orElseThrow(() -> new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND))
				.getSeq();
		PaymentWebhook paymentWebhook = webhookDto.toEntity();
		paymentWebhook.setPaymentSeq(seq);
		webhookRepository.save(paymentWebhook);
	}
}
