package sigma.Spring_backend.crdiPage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkReq;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkRes;
import sigma.Spring_backend.crdiPage.service.CrdiPageService;

@Slf4j
@Api(tags = "5. 코디네이터 작품")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/crdi/work")
public class CrdiPageController {

	private final ResponseService responseService;
	private final CrdiPageService crdiPageService;
	private final int FAIL = -1;

	@PostMapping
	@ApiOperation(value = "코디네이터 작품 등록", notes = "코디네이터의 작품을 등록합니다.")
	public CommonResult registCrdiWork(
			@ApiParam(value = "코데네이터 작품 요청 객체") @ModelAttribute CrdiWorkReq crdiWorkReq,
			@ApiParam(value = "이미지 파일") @RequestParam MultipartFile imageFile
	) {
		try {
			crdiPageService.registCrdiWork(crdiWorkReq, imageFile);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@GetMapping
	@ApiOperation(value = "코디네이터 작품 조회", notes = "코디네이터의 작품을 조회합니다.")
	public CommonResult getCrdiWork(
			@ApiParam(value = "코데네이터 작품 PK") @RequestParam Long workSeq
	) {
		try {
			return responseService.getSingleResult(crdiPageService.getWork(workSeq));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/all")
	@ApiOperation(value = "코디네이터 작품 전체 조회", notes = "코디네이터 작품을 전체 조회합니다.")
	public ListResult<CrdiWorkRes> getCrdiWorks(
			@ApiParam(value = "코디 이메일") @RequestParam String crdiEmail
	) {
		try {
			return responseService.getListResult(crdiPageService.getWorks(crdiEmail));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
