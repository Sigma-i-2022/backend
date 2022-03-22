package sigma.Spring_backend.account.dto;

import lombok.Data;
import sigma.Spring_backend.account.entity.OpenApiAccessToken;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Data
public class OpenApiAccessTokenDto {
	String access_token;	// 오픈뱅킹에서 발급해준 엑세스 토큰
	String token_type;		// 토큰유형 Bearer
	String expires_in;		// 토큰 만료 기간 (초)
	String scope;			// 토큰 권한 범위 oob
	String client_use_code;	// 이용기관코드

	public OpenApiAccessToken toEntity() {
		LocalDateTime expireDate = LocalDateTime
				.now(ZoneId.of("Asia/Seoul"))
				.plusSeconds(Integer.parseInt(expires_in));

		return OpenApiAccessToken.builder()
				.accessToken(access_token)
				.tokenType(token_type)
				.expireDate(expireDate.toString())
				.scope(scope)
				.clientUseCode(client_use_code)
				.build();
	}
}