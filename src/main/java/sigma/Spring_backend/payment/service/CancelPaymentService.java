package sigma.Spring_backend.payment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.payment.dto.PaymentResHandleDto;
import sigma.Spring_backend.payment.repository.PaymentRepository;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class CancelPaymentService {

	private final PaymentRepository paymentRepository;

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
	public boolean requestPaymentCancel(String paymentKey, String cancelReason) {
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
			throw new BussinessException(e.getMessage().split(": ")[1]);
		}

		if (paymentCancelResDto == null) return false;

		Long cancelAmount = paymentCancelResDto.getCancels()[0].getCancelAmount();
		try {
			paymentRepository
					.findByPaymentKey(paymentKey)
					.filter(P -> P.getAmount().equals(cancelAmount))
					.orElseThrow(() -> new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND))
					.getCustomer()
					.addCancelPayment(paymentCancelResDto.toCancelPayment());
			return true;
		} catch (Exception e) {
			throw new BussinessException(ExMessage.DB_ERROR_SAVE);
		}
	}
}
