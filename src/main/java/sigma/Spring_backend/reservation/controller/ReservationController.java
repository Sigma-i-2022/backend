package sigma.Spring_backend.reservation.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.reservation.dto.ReservePartTimeReq;
import sigma.Spring_backend.reservation.dto.ReserveReq;
import sigma.Spring_backend.reservation.dto.ReserveRes;
import sigma.Spring_backend.reservation.service.ReservationService;

@Api(tags = "07. 예약")
@RestController
@RequestMapping("/v1/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

	private final ReservationService reservationService;
	private final ResponseService responseService;
	private final int FAIL = -1;

	@GetMapping
	@ApiOperation(value = "예약 조회", notes = "SEQ를 이용하여 예약 조회")
	public SingleResult<ReserveRes> getReserveBySeq(
			@ApiParam(value = "예약 SEQ", required = true) @RequestParam Long seq
	) {
		try {
			return responseService.getSingleResult(reservationService.getReservation(seq));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/common/list")
	@ApiOperation(value = "공통 예약 목록 조회", notes = "코디/고객 별 예약 목록을 조회합니다.")
	public ListResult<ReserveRes> getAllReserveOfCrdi(
			@ApiParam(value = "회원 이메일") @RequestParam String email,
			@ApiParam(value = "PAGE 번호 (0부터)", required = true) @RequestParam(defaultValue = "0") int page,
			@ApiParam(value = "PAGE 크기", required = true) @RequestParam(defaultValue = "20") int size
	) {
		PageRequest pageRequest = PageRequest.of(page, size, Sort.by("seq").descending());
		try {
			return responseService.getListResult(reservationService.getAllReservationOfMember(email, pageRequest));
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
	@ApiOperation(value = "예약 내역 삭제", notes = "고객/코디 별 예약 목록에서 예약을 삭제합니다.")
	public CommonResult removeReserveOfClient(
			@ApiParam(value = "회원 이메일", required = true) @RequestParam String memberEmail,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq
	) {
		try {
			boolean success = reservationService.hideReservation(memberEmail, reservationSeq);

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
			@ApiParam(value = "코디 이메일", required = true) @RequestParam String crdiEmail,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq,
			@ApiParam(value = "확정 시간", required = true) @RequestBody ReservePartTimeReq resvTime
	) {
		try {
			reservationService.confirmReservation(crdiEmail, reservationSeq, resvTime);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@PostMapping("/client/pay")
	@ApiOperation(value = "고객 예약상품 구매 확정", notes = "고객이 예약완료된 예약상품의 구매를 완료합니다.")
	public CommonResult confirmPayByClient(
			@ApiParam(value = "고객 이메일", required = true) @RequestParam String clientEmail,
			@ApiParam(value = "예약 번호", required = true) @RequestParam Long reservationSeq
	) {
		try {
			boolean success = reservationService.confirmPay(clientEmail, reservationSeq);

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
}
