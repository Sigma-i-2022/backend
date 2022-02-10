package sigma.Spring_backend.advice.exception;

import sigma.Spring_backend.advice.BussinessExceptionMessage;

public class BussinessException extends RuntimeException {

	public BussinessException(BussinessExceptionMessage message) {
		super(message.getMessage());
	}

	public BussinessException(String message) {
		super(message);
	}
}
