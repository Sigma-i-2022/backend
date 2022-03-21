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
import sigma.Spring_backend.payment.repository.PaymentRepository;

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
		String payType = paymentReq.getPayType().name();
		String customerEmail = paymentReq.getCustomerEmail();
		String orderName = paymentReq.getOrderName().name();

		if (amount == null || amount != 3000) {
			throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_PRICE);
		}

		if (!payType.equals("CARD") && !payType.equals("카드")) {
			throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_PAY_TYPE);
		}

		if (!orderName.equals(OrderNameType.CRDI_OR_PRODUCT_RECMD.name()) &&
				!orderName.equals(OrderNameType.STYLE_FEEDBACK.name())) {
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
							throw new BussinessException(ExMessage.UNDEFINED_ERROR);
						}
				);
	}

	@Transactional
	public PaymentResHandleDto requestFinalPayment(String paymentKey, String orderId, Long amount) {
		RestTemplate rest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		testSecretApiKey = testSecretApiKey + ":";
		String encodedAuth = new String(Base64.getEncoder().encode(testSecretApiKey.getBytes(StandardCharsets.UTF_8)));
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

		PaymentResHandleCardDto card = payResDto.getCard();
		paymentRepository.findByOrderId(payResDto.getOrderId())
				.ifPresent(payment -> {
					payment.setCardCompany(card.getCompany());
					payment.setCardNumber(card.getNumber());
					payment.setCardReceiptUrl(card.getReceiptUrl());
				});

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
}
