package sigma.Spring_backend.memberMypage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.advice.BussinessExceptionMessage;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberMypage.dto.MemberProfileImgReq;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberMypage.service.MemberMypageService;
import sigma.Spring_backend.baseUtil.service.ResponseService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "3. 회원 마이페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class MemberMypageController {

	private final ResponseService responseService;
	private final MemberMypageService memberMypageService;
	private final int FAIL = -1;

	@GetMapping("/mypage")
	@ApiOperation(value = "회원 마이페이지 조회", notes = "회원의 마이페이지를 가져옵니다.")
	public SingleResult<MemberMypage> memberMypageGet(
			@ApiParam(value = "회원 이메일", required = true) @RequestParam String memberEmail,
			@ApiParam(value = "회원 아이디", required = true) @RequestParam String memberId
	) {
		Map<String, String> memberMypageInfoMap = new HashMap<>();
		memberMypageInfoMap.put("email", memberEmail);
		memberMypageInfoMap.put("userId", memberId);
		try {
			MemberMypage mypage = memberMypageService.getMemberProfile(memberMypageInfoMap);
			if (mypage != null) {
				return responseService.getSingleResult(mypage);
			} else {
				throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/mypage")
	@ApiOperation(value = "회원 마이페이지 등록", notes = "회원의 마이페이지를 등록합니다.")
	public CommonResult memberMypageRegist(
			@ApiParam(value = "회원 이메일", required = true) @RequestParam String memberEmail,
			@ApiParam(value = "회원 아이디", required = true) @RequestParam String memberId,
			@ApiParam(value = "회원 자기소개") @RequestParam String memberIntroPart
	) {
		Map<String, String> memberMypageInfoMap = new HashMap<>();
		memberMypageInfoMap.put("email", memberEmail);
		memberMypageInfoMap.put("userId", memberId);
		memberMypageInfoMap.put("intro", memberIntroPart);

		try {
			memberMypageService.registMemberMypage(memberMypageInfoMap);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@PutMapping("/mypage")
	@ApiOperation(value = "회원의 마이페이지 수정", notes = "회원의 마이페이지의 소개란을 수정합니다.")
	public CommonResult memberMypageUpdate(
			@ApiParam(value = "회원 이메일", required = true) @RequestParam String memberEmail,
			@ApiParam(value = "회원 아이디", required = true) @RequestParam String memberId,
			@ApiParam(value = "회원 자기소개") @RequestParam String memberIntroPart
	) {
		try {
			Map<String, String> memberMypageInfoMap = new HashMap<>();
			memberMypageInfoMap.put("email", memberEmail);
			memberMypageInfoMap.put("userId", memberId);
			memberMypageInfoMap.put("intro", memberIntroPart);

			memberMypageService.updateMemberProfile(memberMypageInfoMap);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@DeleteMapping("/mypage")
	@ApiOperation(value = "회원의 마이페이지 삭제", notes = "회원의 마이페이지를 삭제합니다..")
	public CommonResult memberMypageDelete(
			@ApiParam(value = "회원 이메일", required = true) @RequestParam String memberEmail,
			@ApiParam(value = "회원 아이디", required = true) @RequestParam String memberId
	) {
		try {
			Map<String, String> memberMypageInfoMap = new HashMap<>();
			memberMypageInfoMap.put("email", memberEmail);
			memberMypageInfoMap.put("userId", memberId);

			memberMypageService.deleteMemberMypage(memberMypageInfoMap);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@PostMapping("/profileImage")
	@ApiOperation(value = "회원 프로필 이미지 등록", notes = "회원의 이미지를 S3에 업로드하고 해당 URL을 등록합니다.")
	public CommonResult registMemberProfileImage(
			@ApiParam(value = "이미지 파일") @ModelAttribute MemberProfileImgReq memberProfileImgReq
	) {
		try {
			memberMypageService.registProfileImage(memberProfileImgReq);
			return responseService.getSuccessResult();
		} catch (Exception e){
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}
}

