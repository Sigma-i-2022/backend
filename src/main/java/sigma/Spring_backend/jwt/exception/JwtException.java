package sigma.Spring_backend.jwt.exception;

import org.springframework.security.core.AuthenticationException;
import sigma.Spring_backend.jwt.dto.JwtError;

public class JwtException extends AuthenticationException {

	public JwtException(JwtError message) {
		super(message.getMessage());
	}

	public JwtException(String message) {
		super(message);
	}

	public JwtException(String message, Throwable cause) {
		super(message, cause);
	}
}
