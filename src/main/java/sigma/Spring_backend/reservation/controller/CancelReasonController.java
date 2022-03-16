package sigma.Spring_backend.reservation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.reservation.dto.CancelReasonDto;
import sigma.Spring_backend.reservation.dto.TYPE;
import sigma.Spring_backend.reservation.service.CancelReasonService;

@Api(tags = "8. 예약 관련 취소")
@RestController
@RequestMapping("/v1/api/cancel")
@RequiredArgsConstructor
public class CancelReasonController {

	private final CancelReasonService cancelReasonService;
	private final ResponseService responseService;

	@GetMapping("/all")
	@ApiOperation(value = "예약 취소 내역 조회", notes = "조건에 맞는 예약 취소내역을 모두 가져옵니다.")
	public ListResult<CancelReasonDto> getAllCancelReasons(
			@ApiParam(value = "조회 조건", required = true)
			@RequestParam(value = "ALL/CRDI/CLIENT") TYPE condition
	) {
		try {
			return responseService.getListResult(cancelReasonService.getAllReason(condition));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
