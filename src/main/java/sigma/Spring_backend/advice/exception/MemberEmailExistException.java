package sigma.Spring_backend.advice.exception;

import sigma.Spring_backend.member.Member;

public class MemberEmailExistException extends RuntimeException{
    public MemberEmailExistException() {
        super();
    }

    public MemberEmailExistException(String message) {
        super(message);
    }

    public MemberEmailExistException(String message, Throwable cause) {
        super(message, cause);
    }

    public MemberEmailExistException(Member member) {

    }
}
