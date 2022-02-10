package sigma.Spring_backend.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BussinessExceptionMessage {

	MEMBER_ERROR_DUPLICATE("회원이 중복되었습니다.")
    , MEMBER_ERROR_NAME_FORMAT("이름 형식이 잘못 되었습니다.")
	, EMAIL_ERROR_FORMAT("이메일 형식이 잘못 되었습니다.")
	, EMAIL_ERROR_SEND("회원가입 이메일 인증을 위한 메일 전송에 실패하였습니다.")
	, MEMBER_MYPAGE_ERROR_REGIST("회원 마이페이지 등록에 실패하였습니다.")
	, MEMBER_MYPAGE_ERROR_UPDATE("회원의 마이페이지를 업데이트하는데 실패했습니다.")
	, MEMBER_MYPAGE_ERROR_DELETE("회원의 마이페이지 삭제에 실패했습니다.");

	private String message;
}
