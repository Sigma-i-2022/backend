package sigma.Spring_backend.payment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.payment.dto.CancelPaymentRes;
import sigma.Spring_backend.payment.dto.TossErrorDto;
import sigma.Spring_backend.payment.service.CancelPaymentService;

import java.util.Optional;

@Api(tags = "12. 결제 취소")
@RequestMapping("/v1/api/cancelPayment")
@RestController
@RequiredArgsConstructor
public class CancelPaymentController {

	private final ResponseService responseService;
	private final CancelPaymentService cancelPaymentService;
	private final int FAIL = -1;

	@PostMapping
	@ApiOperation(
			value = "(공통) 코디네이터 예약 결제 완료 건 결제취소",
			notes = "코디네이터가 예약확정되지 않은 완료된 결제 건에 대해서 결제취소를 요청합니다.<br>" +
					"고객이 예약확정 된 결제를 시작하기 6시간 전까지 취소합니다.<br>" +
					"고객이 예약확정 되지 않은 결제를 취소합니다."
	)
	public CommonResult requestPaymentCancel(
			@ApiParam(value = "토스 측 주문 고유 번호", required = true) @RequestParam String paymentKey,
			@ApiParam(value = "결제 취소 사유", required = true) @RequestParam String cancelReason,
			@ApiParam(value = "코디 번호", required = true) @RequestParam Long memberSeq,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq
	) {
		String resultMessage = cancelPaymentService.requestPaymentCancel(paymentKey, cancelReason, memberSeq, reservationSeq);
		if (resultMessage.equals("성공")) {
			return responseService.getSuccessResult();
		} else return responseService.getFailResult(
				-1,
				resultMessage
		);
	}

	@GetMapping("/{memberSeq}")
	@ApiOperation(value = "고객 예약 취소 목록 전체 조회")
	public ListResult<CancelPaymentRes> getAllCancelPayments(
			@ApiParam(value = "고객 번호", required = true) @PathVariable(name = "memberSeq") Long memberSeq
	) {
		try {
			return responseService.getListResult(
					cancelPaymentService.getAllCancelPayments(memberSeq)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
