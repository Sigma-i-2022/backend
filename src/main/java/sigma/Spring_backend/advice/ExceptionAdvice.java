package sigma.Spring_backend.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sigma.Spring_backend.advice.exception.BussinessException;
import sigma.Spring_backend.advice.exception.EmailException;
import sigma.Spring_backend.advice.exception.MemberEmailExistException;
import sigma.Spring_backend.advice.exception.MemberNotFoundException;
import sigma.Spring_backend.entity.base.CommonResult;
import sigma.Spring_backend.service.base.ResponseService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {
	private final ResponseService responseService;

	/*
	Error Code : -9999
	Message : undefined error
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult undefinedException(HttpServletRequest request, Exception e) {

		log.error(request.getRequestURI());
		log.error(e.getMessage());

		return responseService.getFailResult(
				ErrorCode.UndefinedException.getCode(),
				ErrorCode.UndefinedException.getMessage()
		);
	}

	/*
	Error Code : -1000
	Message : Not Found Member
	 */
	@ExceptionHandler(MemberNotFoundException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult memberNotFoundException(HttpServletRequest request, Exception e) {
		return responseService.getFailResult(
				ErrorCode.MemberNotFoundException.getCode(),
				ErrorCode.MemberNotFoundException.getMessage()
		);
	}

	/*
	Error Code : -1001
	Message : Member Email Exist
	 */
	@ExceptionHandler(MemberEmailExistException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult memberEmailExistException(HttpServletRequest request, Exception e) {
		return responseService.getFailResult(
				ErrorCode.MemberEmailExistException.getCode(),
				ErrorCode.MemberEmailExistException.getMessage()
		);
	}

	/*
	Error Code : -1002
	Message : Email Exception
	 */
	@ExceptionHandler(EmailException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult emailException(HttpServletRequest request, Exception e) {
		return responseService.getFailResult(
				ErrorCode.EmailException.getCode(),
				ErrorCode.EmailException.getMessage()
		);
	}

	/*
	비지니스 예외 처리 통합.
	시간 단축을 위해 하나로 처리, 내용만 적어서 처리해준다.
	 */
	@ExceptionHandler(BussinessException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public CommonResult bussinessException(HttpServletRequest request, Exception e) {
		return responseService.getFailResult(
				-1,
				e.getMessage()
		);
	}

}
