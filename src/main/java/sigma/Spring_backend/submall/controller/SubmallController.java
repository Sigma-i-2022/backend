package sigma.Spring_backend.submall.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.submall.dto.SubmallReqDto;
import sigma.Spring_backend.submall.dto.SubmallResDto;
import sigma.Spring_backend.submall.dto.TosspaymentSubmallRes;
import sigma.Spring_backend.submall.service.SubmallService;

@Slf4j
@RestController
@RequiredArgsConstructor
@Api(tags = "14. 서브몰 관리")
@RequestMapping("/v1/api/submall")
public class SubmallController {

	private final SubmallService submallService;
	private final ResponseService responseService;

	@PostMapping
	@ApiOperation(value = "서브몰 등록", notes = "판매자가 은행, 계좌번호 등을 이용해 자신의 서브몰을 등록합니다.")
	public SingleResult<SubmallResDto> registSubmall(
			@ApiParam(value = "요청 객체") @ModelAttribute SubmallReqDto submallReqDto
	) {
		try {
			return responseService.getSingleResult(
					submallService.registSubmall(submallReqDto)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping
	@ApiOperation(value = "서브몰 조회", notes = "판매자의 서브몰을 조회합니다.")
	public SingleResult<SubmallResDto> registSubmall(
			@ApiParam(value = "코디네이터 이메일", required = true) @RequestParam String crdiEmail
	) {
		try {
			return responseService.getSingleResult(
					submallService.getSubmall(crdiEmail)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/all")
	@ApiOperation(value = "모든 서브몰 조회", notes = "토스페이먼츠 API를 통해 모든 서브몰을 조회합니다.")
	public ListResult<TosspaymentSubmallRes> getAllSubmall() {
		try {
			return responseService.getListResult(
					submallService.getAllSubmall()
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}


	@PutMapping
	@ApiOperation(value = "서브몰 수정", notes = "판매자가 자신의 서브몰을 수정합니다.")
	public SingleResult<SubmallResDto> updateSubmall(
			@ApiParam(value = "요청 객체") @ModelAttribute SubmallReqDto submallReqDto
	) {
		try {
			return responseService.getSingleResult(
					submallService.updateSubmall(submallReqDto)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@DeleteMapping
	@ApiOperation(value = "서브몰 삭제", notes = "판매자가 자신의 서브몰을 삭제합니다.")
	public SingleResult<String> deleteSubmall(
			@ApiParam(value = "코디네이터 이메일", required = true) @RequestParam String crdiEmail
	) {
		try {
			return responseService.getSingleResult(
					submallService.deleteSubmall(crdiEmail)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
