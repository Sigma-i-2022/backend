package sigma.Spring_backend.payment.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.payment.dto.CancelPaymentRes;
import sigma.Spring_backend.payment.dto.CancelPaymentReq;
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
	@ApiOperation(
			value = "(공통) 코디네이터 예약 결제 완료 건 결제취소",
			notes = "코디네이터가 예약확정되지 않은 완료된 결제 건에 대해서 결제취소를 요청합니다.<br>" +
					"고객이 예약확정 된 결제를 시작하기 6시간 전까지 취소합니다.<br>" +
					"고객이 예약확정 되지 않은 결제를 취소합니다."
	)
	public CommonResult requestPaymentCancel(
			@ApiParam(value = "코디 번호", required = true) @RequestParam Long memberSeq,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq,
			@ApiParam(value = "가상계좌 취소시 작성") @ModelAttribute CancelPaymentReq cancelPaymentReq
			) {
		try {
			cancelPaymentService.requestPaymentCancel(memberSeq, reservationSeq, cancelPaymentReq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@GetMapping("/{memberSeq}")
	@ApiOperation(value = "고객 예약 취소 목록 전체 조회", notes = "고객이 취소한 모든 예약 목록을 조회합니다.")
	public ListResult<CancelPaymentRes> getAllCancelPayments(
			@ApiParam(value = "고객 번호", required = true) @PathVariable(name = "memberSeq") Long memberSeq,
			@ApiParam(value = "PAGE 번호 (0부터)", required = true) @RequestParam(defaultValue = "0") int page,
			@ApiParam(value = "PAGE 크기", required = true) @RequestParam(defaultValue = "20") int size
	) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("cancelDate").descending());
		try {
			return responseService.getListResult(
					cancelPaymentService.getAllCancelPayments(memberSeq, pageRequest)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
