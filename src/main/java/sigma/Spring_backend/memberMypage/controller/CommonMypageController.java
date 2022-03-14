package sigma.Spring_backend.memberMypage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberMypage.dto.*;
import sigma.Spring_backend.memberMypage.service.CommonMypageServiceImpl;
import sigma.Spring_backend.baseUtil.service.ResponseService;

@Slf4j
@Api(tags = "3. 마이페이지 공통")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/mypage")
public class CommonMypageController {

	private final ResponseService responseService;
	private final CommonMypageServiceImpl commonMypageServiceImpl;
	private final int FAIL = -1;

	@GetMapping("/client")
	@ApiOperation(value = "고객 마이페이지 조회", notes = "고객의 마이페이지를 가져옵니다.")
	public SingleResult<ClientMypageRes> memberMypageGet(
			@ApiParam(value = "고객 번호", required = true) @RequestParam String email
	) {
		try {
			return responseService.getSingleResult(commonMypageServiceImpl.getClientMypage(email));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PutMapping("/client")
	@ApiOperation(value = "고객 마이페이지 소개란 수정", notes = "고객의 마이페이지의 소개란을 수정합니다.")
	public CommonResult clientMypageUpdate(
			@ApiParam(value = "고객마이페이지수정", required = true) @ModelAttribute CommonUpdateInfoReq updateInfoReq
	) {
		try {
			commonMypageServiceImpl.updateClientMypageInfo(updateInfoReq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@GetMapping("/crdi")
	@ApiOperation(value = "코디네이터 마이페이지 조회", notes = "코디네이터의 마이페이지를 조회합니다.")
	public SingleResult<CrdiMypageRes> crdiMypageGet(
			@ApiParam(value = "코디네이터 번호", required = true) @RequestParam String email
	) {
		try {
			return responseService.getSingleResult(commonMypageServiceImpl.getCrdiMypage(email));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/crdi")
	@ApiOperation(value = "코디네이터 마이페이지 등록", notes = "코디네이터의 마이페이지를 등록합니다.")
	public CommonResult crdiMypageRegist(
			@ApiParam(name = "코디마이페이지등록") @ModelAttribute CrdiMypageReq crdiMypageReq
	) {
		try {
			commonMypageServiceImpl.registCrdiMypage(crdiMypageReq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@PutMapping("/crdi")
	@ApiOperation(value = "코디네이터 마이페이지 소개란 수정", notes = "코디네이터의 마이페이지 소개란을 수정합니다.")
	public CommonResult crdiMypageUpdate(
			@ApiParam(name = "코디마이페이지수정") @ModelAttribute CommonUpdateInfoReq updateInfoReq
	) {
		try {
			commonMypageServiceImpl.updateCrdiMypageInfo(updateInfoReq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@PostMapping("/image")
	@ApiOperation(value = "프로필 이미지 등록/수정", notes = "회원의 이미지를 S3에 업로드하고 해당 URL을 등록/수정합니다.")
	public CommonResult registMemberProfileImage(
			@ApiParam(value = "이미지 파일") @ModelAttribute CommonProfileImgReq commonProfileImgReq
	) {
		try {
			commonMypageServiceImpl.updateProfileImg(commonProfileImgReq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}
}

