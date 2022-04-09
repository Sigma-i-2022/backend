package sigma.Spring_backend.submall.service;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;
import sigma.Spring_backend.payment.dto.TossErrorDto;
import sigma.Spring_backend.submall.dto.SubmallReqDto;
import sigma.Spring_backend.submall.dto.SubmallResDto;
import sigma.Spring_backend.submall.dto.TosspaymentSubmallReq;
import sigma.Spring_backend.submall.dto.TosspaymentSubmallRes;
import sigma.Spring_backend.submall.entity.Submall;
import sigma.Spring_backend.submall.repository.SubmallRepository;

import java.nio.charset.StandardCharsets;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmallService {

	private final SubmallRepository submallRepository;
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
	public SubmallResDto registSubmall(SubmallReqDto submallReqDto) {

		if (submallRepository.findByCrdiEmail(submallReqDto.getCrdiEmail()).isPresent()) {
			throw new BussinessException("고객님 앞으로 서브몰이 이미 등록되어있습니다.");
		}

		if (!(submallReqDto.getType().equals("법인") || submallReqDto.getType().equals("개인"))) {
			throw new BussinessException("잘못된 사업자 유형 입니다.");
		}

		if (submallReqDto.getType().equals("법인") && submallReqDto.getBusinessNumber().length() != 10) {
			throw new BussinessException("잘못된 사업자 번호 입니다.");
		}

		RestTemplate rest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		String encodedAuth = new String(Base64.getEncoder().encode((testSecretApiKey + ":").getBytes(StandardCharsets.UTF_8)));
		headers.setBasicAuth(encodedAuth);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		TosspaymentSubmallReq submallReq = submallReqDto.toTossReq();
		TosspaymentSubmallRes subMallRes;
		try {
			subMallRes = rest.exchange(
					tossOriginUrl + "/payouts/sub-malls",
					HttpMethod.POST,
					new HttpEntity<>(new Gson().toJson(submallReq), headers),
					TosspaymentSubmallRes.class
			).getBody();
			if (subMallRes == null) throw new BussinessException("NULL");
		} catch (Exception e) {
			e.printStackTrace();
			String errorResponse = e.getMessage().split(": ")[1];
			String errorMessage = new Gson()
					.fromJson(
							errorResponse.substring(1, errorResponse.length() - 1),
							TossErrorDto.class
					).getMessage();
			throw new BussinessException(errorMessage);
		}

		try {
			Submall submall = submallReqDto.toEntity(submallReq.getSubMallId());
			memberRepository.findByEmailFJ(submallReqDto.getCrdiEmail())
					.ifPresentOrElse(
							C -> C.setSubmall(submall)
							, () -> {
								throw new BussinessException(ExMessage.UNDEFINED_ERROR);
							});
			return submall.toDto();
		} catch (Exception e) {
			throw new BussinessException(ExMessage.DB_ERROR_SAVE);
		}
	}

	@Transactional(readOnly = true)
	public SubmallResDto getSubmall(String crdiEmail) {
		return submallRepository.findByCrdiEmail(crdiEmail)
				.orElseThrow(() -> new BussinessException(ExMessage.UNDEFINED_ERROR))
				.toDto();
	}

	@Transactional(readOnly = true)
	public List<TosspaymentSubmallRes> getAllSubmall() {
		RestTemplate rest = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();

		String encodedAuth = new String(Base64.getEncoder().encode((testSecretApiKey + ":").getBytes(StandardCharsets.UTF_8)));
		headers.setBasicAuth(encodedAuth);
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		TosspaymentSubmallRes[] submallRes;
		try {
			submallRes = rest.exchange(
					tossOriginUrl + "/payouts/sub-malls",
					HttpMethod.GET,
					new HttpEntity<>(headers),
					TosspaymentSubmallRes[].class
			).getBody();
		} catch (Exception e) {
			String errorResponse = e.getMessage().split(": ")[1];
			String errorMessage = new Gson()
					.fromJson(
							errorResponse.substring(1, errorResponse.length() - 1),
							TossErrorDto.class
					).getMessage();
			throw new BussinessException(errorMessage);
		}

		if (submallRes == null || submallRes.length == 0) {
			return new ArrayList<>();
		}

		return Arrays.asList(submallRes);
	}

	public SubmallResDto updateSubmall(SubmallReqDto submallReqDto) {
		return null;
	}

	public String deleteSubmall(String crdiEmail) {
		return null;
	}
}
