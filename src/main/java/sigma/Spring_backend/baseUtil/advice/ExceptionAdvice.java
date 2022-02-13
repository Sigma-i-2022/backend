package sigma.Spring_backend.baseUtil.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.service.ResponseService;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

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
        log.error(Arrays.toString(e.getStackTrace()));

        return responseService.getFailResult(
                -9999,
                BussinessExceptionMessage.UNDEFINED_ERROR.getMessage()
        );
    }

    /*
    BussinessException 통합 처리
     */
    @ExceptionHandler(BussinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult bussinessException(HttpServletRequest request, Exception e) {

        log.error(request.getRequestURI());
        log.error(Arrays.toString(e.getStackTrace()));

        return responseService.getFailResult(
                -1,
                e.getMessage()
        );
    }
}
