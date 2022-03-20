package sigma.Spring_backend.payment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.payment.service.CancelPaymentService;

@Api(tags = "13. 결제 취소")
@RequestMapping("/v1/api/cancelPayment")
@RestController
@RequiredArgsConstructor
public class CancelPaymentController {

	private final ResponseService responseService;
	private final CancelPaymentService cancelPaymentService;
	private final int FAIL = -1;

	@PostMapping
	@ApiOperation(value = "결제 취소", notes = "완료 된 결제 건에 대해서 결제취소를 요청합니다.")
	public CommonResult requestPaymentCancel(
			@ApiParam(value = "토스 측 주문 고유 번호", required = true) @RequestParam String paymentKey,
			@ApiParam(value = "결제 취소 사유", required = true) @RequestParam String cancelReason
	) {
		boolean result = cancelPaymentService.requestPaymentCancel(paymentKey, cancelReason);
		if (result) {
			return responseService.getSuccessResult();
		} else return responseService.getFailResult(
				FAIL,
				ExMessage.PAYMENT_CANCEL_ERROR_FAIL.getMessage()
		);
	}
}
