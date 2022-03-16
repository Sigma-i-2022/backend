package sigma.Spring_backend.reservation.controller;

import com.sun.istack.NotNull;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.reservation.dto.ReservePartTimeReq;
import sigma.Spring_backend.reservation.dto.ReserveReq;
import sigma.Spring_backend.reservation.dto.ReserveRes;
import sigma.Spring_backend.reservation.service.ReservationService;

@Api(tags = "7. 예약")
@RestController
@RequestMapping("/v1/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;
	private final ResponseService responseService;
	private final int FAIL = -1;

	@GetMapping("/common/all")
	@ApiOperation(value = "예약 목록 전체 조회", notes = "모든 예약을 조회합니다.")
	public ListResult<ReserveRes> getAllResv() {
		try {
			return responseService.getListResult(reservationService.getAllReservations());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/common/list")
	@ApiOperation(value = "회원 예약 목록 조회", notes = "코디/고객 별 예약 목록을 조회합니다.")
	public ListResult<ReserveRes> getAllReserveOfCrdi(
			@ApiParam(value = "회원 아이디") @RequestParam String memberId
	) {
		try {
			return responseService.getListResult(reservationService.getAllReservationOfMember(memberId));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/client")
	@ApiOperation(value = "고객 코디네이터예약 신청", notes = "고객이 코디네이터에게 코디 예약을 합니다.")
	public CommonResult reserveCrdi(
			@ApiParam(value = "코디 예약 신청", required = true) @RequestBody ReserveReq reserveReq
	) {
		try {
			boolean success = reservationService.reserveCrdi(reserveReq);

			if (success) {
				return responseService.getSuccessResult();
			} else {
				return responseService.getFailResult(
						-1,
						ExMessage.RESERVATION_ERROR.getMessage()
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/common/hide")
	@ApiOperation(value = "예약 내역 지움", notes = "고객/코디 별 예약 목록에서 단건 씩 예약을 지웁니다.")
	public CommonResult removeReserveOfClient(
			@ApiParam(value = "회원 아이디", required = true) @RequestParam String memberId,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq
	) {
		try {
			boolean success = reservationService.hideReservation(memberId, reservationSeq);

			if (success) {
				return responseService.getSuccessResult();
			} else {
				return responseService.getFailResult(
						FAIL,
						ExMessage.RESERVATION_ERROR_HIDE.getMessage()
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/crdi/fix")
	@ApiOperation(value = "코디네이터 예약 확정", notes = "코디네이터가 신청받은 예약을 확정합니다.")
	public CommonResult confirmResvByCrdi(
			@ApiParam(value = "코디 아이디", required = true) @RequestParam String crdiId,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq,
			@ApiParam(value = "확정 시간", required = true) @RequestBody ReservePartTimeReq resvTime
	) {
		try {
			boolean success = reservationService.confirmReservation(crdiId, reservationSeq, resvTime);

			if (success) {
				return responseService.getSuccessResult();
			} else {
				return responseService.getFailResult(
						FAIL,
						ExMessage.RESERVATION_ERROR_CANCEL_CASE_CRDI.getMessage()
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/client/pay")
	@ApiOperation(value = "고객 예약상품 구매 확정", notes = "고객이 예약완료된 예약상품의 구매를 완료합니다.")
	public CommonResult confirmPayByClient(
			@ApiParam(value = "고객 아이디", required = true) @RequestParam String clientId,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq
	) {
		try {
			boolean success = reservationService.confirmPay(clientId, reservationSeq);

			if (success) {
				return responseService.getSuccessResult();
			} else {
				return responseService.getFailResult(
						FAIL,
						ExMessage.RESERVATION_ERROR_CANCEL_CASE_CRDI.getMessage()
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/crdi/cancel")
	@ApiOperation(value = "코디네이터 예약 취소", notes = "코디네이터가 예약확정되지 않은 예약에 한해서 취소(반려)합니다.")
	public CommonResult cancelByCrdi(
			@ApiParam(value = "코디 아이디", required = true) @RequestParam String crdiId,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq,
			@ApiParam(value = "취소 사유", required = true) @RequestParam String reason
	) {
		try {
			boolean success = reservationService.cancelResvByCrdi(crdiId, reservationSeq, reason);

			if (success) {
				return responseService.getSuccessResult();
			} else {
				return responseService.getFailResult(
						FAIL,
						ExMessage.RESERVATION_ERROR_CANCEL_CASE_CRDI.getMessage()
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/client/cancel")
	@ApiOperation(
			value = "고객 예약 취소",
			notes = "고객이 예약확정 된 예약을 시작하기 6시간 전까지 취소합니다.<br>" +
					"고객이 예약확정 되지 않은 예약을 취소합니다."
	)
	public CommonResult cancelByClient(
			@ApiParam(value = "고객 아이디", required = true) @RequestParam String clientId,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq,
			@ApiParam(value = "예약 번호", required = true) @RequestParam String reason

	) {
		try {
			boolean success = reservationService.cancelResvByClient(clientId, reservationSeq, reason);
			if (success) {
				return responseService.getSuccessResult();
			} else {
				return responseService.getFailResult(
						FAIL,
						ExMessage.RESERVATION_ERROR_CANCEL_CASE_CLIENT.getMessage()
				);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
