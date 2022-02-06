package sigma.Spring_backend.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    UndefinedException(-9999, "미정의 에러"),
    MemberNotFoundException(-1000, "저장된 회원이 없습니다."),
    MemberEmailExistException(-1001, "이미 존재하는 이메일입니다."),
    EmailException(-1002, "이메일 인증 코드 관련 오류");

    private int code;
    private String message;
}
