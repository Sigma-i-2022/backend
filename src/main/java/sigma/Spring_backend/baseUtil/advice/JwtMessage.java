package sigma.Spring_backend.baseUtil.advice;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum JwtMessage {

	JWT_INVALID("토큰이 유효하지 않습니다.")
	,
	;

	private final String code;
}
