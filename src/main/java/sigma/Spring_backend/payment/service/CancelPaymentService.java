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
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;
import sigma.Spring_backend.payment.dto.CancelPaymentRes;
import sigma.Spring_backend.payment.dto.PaymentResHandleDto;
import sigma.Spring_backend.payment.dto.TossErrorDto;
import sigma.Spring_backend.payment.entity.CancelPayment;
import sigma.Spring_backend.payment.repository.CancelPaymentRepository;
import sigma.Spring_backend.payment.repository.PaymentRepository;
import sigma.Spring_backend.reservation.entity.Reservation;
import sigma.Spring_backend.reservation.repository.ReservationRepo;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelPaymentService {

	private final PaymentRepository paymentRepository;
	private final ReservationRepo reservationRepo;
	private final MemberRepository memberRepository;
	private final CancelPaymentRepository cancelPaymentRepository;

	@Value("${payments.toss.test_client_api_key}")
	private String testClientApiKey;

	@Value("${payments.toss.test_secret_api_key}")
	private String testSecretApiKey;

	@Value("${payments.toss.live_client_api_key}")
	private String liveClientApiKey;

	@Value("${payments.toss.live_secret_api_key}")
	private String liveSecretApiKey;

	@Value("${payments.toss.origin_url}")
	private String tossOriginUrl;

	@Transactional
	public String requestPaymentCancel(String paymentKey, String cancelReason, Long memberSeq, Long reservationSeq) {
		// 예약 취소
		cancelReservation(memberSeq, reservationSeq);

		// 토스페이먼츠에게 취소 요청
		RestTemplate rest = new RestTemplate();

		URI uri = URI.create(tossOriginUrl + paymentKey + "/cancel");

		HttpHeaders headers = new HttpHeaders();
		byte[] secretKeyByte = (testSecretApiKey + ":").getBytes(StandardCharsets.UTF_8);
		headers.setBasicAuth(new String(Base64.getEncoder().encode(secretKeyByte)));
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		JSONObject param = new JSONObject();
		param.put("cancelReason", cancelReason);

		PaymentResHandleDto paymentCancelResDto;

		try {
			paymentCancelResDto = rest.postForObject(
					uri,
					new HttpEntity<>(param, headers),
					PaymentResHandleDto.class
			);
		} catch (Exception e) {
			String errorResponse = e.getMessage().split(": ")[1];
			return new Gson()
					.fromJson(
							errorResponse.substring(1, errorResponse.length() - 1),
							TossErrorDto.class
					).getMessage();
		}

		if (paymentCancelResDto == null) {
			return "응답값이 비어있습니다.";
		}

		Long cancelAmount = paymentCancelResDto.getCancels()[0].getCancelAmount();
		try {
			cancelPaymentSave(paymentKey, paymentCancelResDto, cancelAmount);
		} catch (Exception e) {
			throw new BussinessException(ExMessage.DB_ERROR_SAVE);
		}

		return "성공";
	}

	private void cancelPaymentSave(String paymentKey, PaymentResHandleDto paymentCancelResDto, Long cancelAmount) {
		paymentRepository
				.findByPaymentKey(paymentKey)
				.filter(P -> P.getAmount().equals(cancelAmount))
				.ifPresentOrElse(P -> {
							P.getCustomer().addCancelPayment(paymentCancelResDto.toCancelPayment());
							P.getCustomer().getPayments()
									.stream().filter(p -> p.getPaymentKey().equals(paymentKey))
									.findFirst()
									.orElseThrow(() -> new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND))
									.setCancelYn("Y");
						}, () -> {
							throw new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND);
						});
	}

	private void cancelReservation(Long memberSeq, Long reservationSeq) {
		Member member = memberRepository.findBySeqFJ(memberSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		if (member.getCrdiYn().equals("Y")) {
			cancelReservationByCrdi(reservationSeq, member);
		} else {
			cancelReservationByClient(reservationSeq, member);
		}
	}

	private void cancelReservationByCrdi(Long reservationSeq, Member member) {
		reservationRepo.findById(reservationSeq)
				.filter(R -> R.getCrdiId().equals(member.getUserId()))
				.filter(R -> R.getConfirmResvYn().equals("N"))
				.ifPresentOrElse(
						R -> {
							R.setCancelYn("Y");
						}, () -> {
							throw new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND);
						});
	}

	private void cancelReservationByClient(Long reservationSeq, Member member) {
		String after6HourOfNow = LocalDateTime.now()
				.plusHours(6)
				.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm"));

		Reservation reservation = reservationRepo.findById(reservationSeq)
				.filter(R -> R.getClientId().equals(member.getUserId()))
				.orElseThrow(() -> new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND_CLIENT));

		String reservationTime = (reservation.getReserveDay() + " " + reservation.getConfirmedReserveTime())
				.substring(0, 19);

		// 6시간 이내에 예약시간 존재하면 취소 불가 or 예약을 안했으면 가능
		if (reservation.getConfirmResvYn().equals("Y") && reservationTime.compareTo(after6HourOfNow) > 0) {
			reservation.setCancelYn("Y");
		} else if (reservation.getConfirmResvYn().equals("N")) {
			reservation.setCancelYn("Y");
		} else {
			throw new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND);
		}
	}

	@Transactional(readOnly = true)
	public List<CancelPaymentRes> getAllCancelPayments(Long memberSeq, PageRequest pageRequest) {
		return cancelPaymentRepository
				.findAllByCustomerSeq(memberSeq, pageRequest)
				.stream().map(CancelPayment::toDto)
				.collect(Collectors.toList());
	}
}
