package sigma.Spring_backend.payment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.payment.dto.PaymentReq;
import sigma.Spring_backend.payment.dto.PaymentRes;
import sigma.Spring_backend.payment.dto.PaymentResHandleDto;
import sigma.Spring_backend.payment.dto.PaymentResHandleFailDto;
import sigma.Spring_backend.payment.service.PaymentService;

@Api(tags = "11. 결제")
@RequestMapping("/v1/api/payment")
@RestController
@RequiredArgsConstructor
public class PaymentController {

	private final PaymentService paymentService;
	private final ResponseService responseService;
	private final int FAIL = -1;

	@PostMapping
	@ApiOperation(value = "결제 요청", notes = "결제 요청에 필요한 값들을 반환합니다.")
	public SingleResult<PaymentRes> requestPayments(
			@ApiParam(value = "요청 객체", required = true) @ModelAttribute PaymentReq paymentReq
	) {
		try {
			return responseService.getSingleResult(
					paymentService.requestPayments(paymentReq)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/success")
	@ApiOperation(value = "결제 성공 리다이렉트", notes = "결제 성공 시 최종 결제 승인 요청을 보냅니다.")
	public SingleResult<PaymentResHandleDto> requestFinalPayments(
			@ApiParam(value = "토스 측 결제 고유 번호", required = true) @RequestParam String paymentKey,
			@ApiParam(value = "우리 측 주문 고유 번호", required = true) @RequestParam String orderId,
			@ApiParam(value = "실제 결제 금액", required = true) @RequestParam Long amount
	) {
		try {
			paymentService.verifyRequest(paymentKey, orderId, amount);
			PaymentResHandleDto result = paymentService.requestFinalPayment(paymentKey, orderId, amount);

			return responseService.getSingleResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/fail")
	@ApiOperation(value = "결제 실패 리다이렉트", notes = "결제 실패 시 에러코드 및 에러메시지를 반환합니다.")
	public SingleResult<PaymentResHandleFailDto> requestFail(
			@ApiParam(value = "에러 코드", required = true) @RequestParam(name = "code") String errorCode,
			@ApiParam(value = "에러 메시지", required = true) @RequestParam(name = "message") String errorMsg,
			@ApiParam(value = "우리 측 주문 고유 번호", required = true) @RequestParam(name = "orderId") String orderId
	) {
		try {
			return responseService.getSingleResult(
					paymentService.requestFail(errorCode, errorMsg, orderId)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
