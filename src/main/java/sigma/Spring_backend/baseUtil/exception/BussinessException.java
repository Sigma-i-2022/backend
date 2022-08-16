package sigma.Spring_backend.baseUtil.exception;

import sigma.Spring_backend.baseUtil.advice.ExMessage;

public class BussinessException extends RuntimeException {

	public BussinessException(ExMessage exMessage) {
		super(exMessage.getMessage());
	}

	public BussinessException(String message) {
		super(message);
	}
}

