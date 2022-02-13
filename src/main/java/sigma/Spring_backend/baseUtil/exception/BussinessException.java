package sigma.Spring_backend.baseUtil.exception;

import sigma.Spring_backend.baseUtil.advice.BussinessExceptionMessage;

public class BussinessException extends RuntimeException {

	public BussinessException(BussinessExceptionMessage message) {
		super(message.getMessage());
	}

	public BussinessException(String message) {
		super(message);
	}
}

