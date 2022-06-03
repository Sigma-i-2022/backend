package sigma.Spring_backend.baseUtil.jwt.exception;

import sigma.Spring_backend.baseUtil.advice.JwtMessage;

public class JwtException extends RuntimeException {
	public JwtException() {
	}

	public JwtException(JwtMessage message) {
		super(message.name());
	}

	public JwtException(String message) {
		super(message);
	}

	public JwtException(String message, Throwable cause) {
		super(message, cause);
	}

	public JwtException(Throwable cause) {
		super(cause);
	}

	public JwtException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}
