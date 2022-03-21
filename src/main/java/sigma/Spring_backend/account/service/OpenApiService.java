package sigma.Spring_backend.account.service;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.CharSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import sigma.Spring_backend.account.dto.OpenApiAccessTokenDto;
import sigma.Spring_backend.account.dto.OpenApiAccountInfoRes;
import sigma.Spring_backend.account.dto.OpenApiAccountRealNameDto;
import sigma.Spring_backend.account.entity.AccountInfo;
import sigma.Spring_backend.account.entity.OpenApiAccessToken;
import sigma.Spring_backend.account.repository.AccessTokenRepository;
import sigma.Spring_backend.account.repository.AccountInfoRepository;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;

import java.net.URI;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class OpenApiService {

	private final AccessTokenRepository accessTokenRepository;
	private final AccountInfoRepository accountInfoRepository;

	@Value("${openapi.client_id}")
	String clientId;

	@Value("${openapi.client_secret}")
	String clientSecret;

	@Value("${openapi.agent_code}")
	String agentCode;

	@Transactional
	public void requestOpenApiAccessToken() {
		RestTemplate rest = new RestTemplate();

		URI uri = URI.create("https://testapi.openbanking.or.kr/oauth/2.0/token");

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		MultiValueMap<String, String> param = new LinkedMultiValueMap<>();
		param.add("client_id", clientId);
		param.add("client_secret", clientSecret);
		param.add("scope", "oob");
		param.add("grant_type", "client_credentials");

		String now = LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString();

		if (accessTokenRepository.findFirstByExpireDateAfter(now).isEmpty()) {
			OpenApiAccessTokenDto newAccessTokenRes;
			try {
				newAccessTokenRes = rest.postForObject(
						uri,
						new HttpEntity<>(param, headers),
						OpenApiAccessTokenDto.class
				);
			} catch (Exception e) {
				throw new BussinessException(e.getMessage());
			}
			accessTokenRepository.save(newAccessTokenRes.toEntity());
		}
	}

	@Transactional
	public boolean requestMatchAccountRealName(Long crdiSeq, String bankCode, String bankAccount, String realName, String birthday) {
		if (birthday.length() != 6 || bankAccount.length() > 16) return false;

		RestTemplate rest = new RestTemplate();

		URI uri = URI.create("https://testapi.openbanking.or.kr/v2.0/inquiry/real_name");

		HttpHeaders headers = new HttpHeaders();
		String accessToken = accessTokenRepository.findFirstByExpireDateAfter(LocalDateTime.now(ZoneId.of("Asia/Seoul")).toString())
				.orElseThrow(() -> new BussinessException(ExMessage.ACCESS_TOKEN_ERROR_NOT_FOUND))
				.getAccessToken();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(accessToken);
		headers.setAcceptCharset(Collections.singletonList(StandardCharsets.UTF_8));
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

		JSONObject param = new JSONObject();
		String uniqueNum = String.valueOf(System.currentTimeMillis() % 1000000000);
		param.put("bank_tran_id", agentCode + "U" + uniqueNum);
		param.put("bank_code_std", bankCode);
		param.put("account_num", bankAccount);
		param.put("account_holder_info_type", "");
		param.put("account_holder_info", birthday);
		param.put("tran_dtime", LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));

		OpenApiAccountRealNameDto realNameDto;
		try {
			realNameDto = rest.postForObject(
					uri,
					new HttpEntity<>(param.toJSONString(), headers),
					OpenApiAccountRealNameDto.class
			);
		} catch (Exception e) {
			throw new BussinessException(e.getMessage());
		}

		if (realNameDto == null) {
			throw new BussinessException(ExMessage.UNDEFINED_ERROR);
		}

		if (!realNameDto.getBank_code_std().equals(bankCode)) {
			System.out.println("bankCode = " + bankCode);
			throw new BussinessException(ExMessage.ACCOUNT_ERROR_WRONG_BANK);
		}

		if (!realNameDto.getAccount_holder_name().equals(realName)) {
			throw new BussinessException(ExMessage.ACCOUNT_ERROR_WRONG_NAME);
		}

		if (!realNameDto.getAccount_holder_info().equals(birthday)) {
			throw new BussinessException(ExMessage.ACCOUNT_ERROR_WRONG_BIRTHDAY);
		}

		accountInfoRepository.findByCrdiSeq(crdiSeq)
				.ifPresentOrElse(
						accountInfo -> {
							accountInfo.setAccountNum(realNameDto.getAccount_num());
							accountInfo.setAccountRealName(realNameDto.getAccount_holder_name());
							accountInfo.setBankName(realNameDto.getBank_name());
							accountInfo.setBankCode(realNameDto.getBank_code_std());
							accountInfo.setBirthDay(realNameDto.getAccount_holder_info());
						}, () -> accountInfoRepository.save(
								AccountInfo.builder()
										.crdiSeq(crdiSeq)
										.accountNum(realNameDto.getAccount_num())
										.accountRealName(realNameDto.getAccount_holder_name())
										.bankCode(realNameDto.getBank_code_std())
										.bankName(realNameDto.getBank_name())
										.birthDay(realNameDto.getAccount_holder_info())
										.build()));
		return true;
	}

	@Transactional(readOnly = true)
	public OpenApiAccountInfoRes getCrdiAccountInfo(Long crdiSeq) {
		return accountInfoRepository.findByCrdiSeq(crdiSeq)
				.stream().map(AccountInfo::toDto)
				.findFirst()
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
	}
}
