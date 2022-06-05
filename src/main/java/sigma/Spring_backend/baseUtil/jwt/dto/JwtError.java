package sigma.Spring_backend.baseUtil.jwt.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtError {

	JWT_ACCESS_NOT_VALID("엑세스 토큰이 유효하지 않습니다.")
	, JWT_REFRESH_NOT_VALID("리프레쉬 토큰이 유효하지 않습니다.")
	, JWT_ACCESS_EXPIRED("엑세스 토큰이 만료되었습니다.")
	, JWT_REFRESH_EXPIRED("토큰이 만료되었습니다. 다시 로그인 해주세요.")
	, JWT_NOT_VALID("토큰이 유효하지 않습니다.")
	, JWT_HEADER_NOT_VALID("헤더가 유효하지 않습니다.")
	, JWT_MEMBER_NOT_FOUND("토큰에 해당하는 회원이 없습니다.")
	;

	private final String message;
}
