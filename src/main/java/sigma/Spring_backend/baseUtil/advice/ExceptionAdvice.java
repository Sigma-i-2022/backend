package sigma.Spring_backend.baseUtil.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
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

        log.error("\n\n========================================ERROR START========================================");
        log.error("요청 URL : " + request.getMethod() + " " + request.getRequestURI());
        log.error("예외 메시지 : " + e.getMessage());
        e.printStackTrace();
        log.error("\n========================================ERROR END========================================\n");

        return responseService.getFailResult(
                -9999,
                e.getMessage()+"\n\n*****************************\n\n"+ Arrays.toString(e.getStackTrace())
        );
    }

    /*
		Multipart Size 예외처리
	 */
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult maxSizeException(HttpServletRequest request, Exception e) {

        log.error("\n========================================ERROR START========================================");
        log.error("요청 URL : " + request.getMethod() + " " + request.getRequestURI());
        log.error("예외 메시지 : " + e.getMessage());
        e.printStackTrace();
        log.error("========================================ERROR END========================================\n");

        return responseService.getFailResult(
                -1,
                ExMessage.MULTIPART_ERROR_SIZE.getMessage()
        );
    }

    /*
    BussinessException 통합 처리
     */
    @ExceptionHandler(BussinessException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public CommonResult bussinessException(HttpServletRequest request, Exception e) {

        log.error("\n\n========================================ERROR START========================================");
        log.error("요청 URL : " + request.getMethod() + " " + request.getRequestURI());
        log.error("예외 메시지 : " + e.getMessage());
        e.printStackTrace();
        log.error("\n========================================ERROR END========================================\n");

        return responseService.getFailResult(
                -1,
                e.getMessage()
        );
    }
}
