package sigma.Spring_backend.memberLook.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.memberLook.dto.MemberLookPageReq;
import sigma.Spring_backend.memberLook.dto.MemberLookPageRes;
import sigma.Spring_backend.memberLook.service.MemberLookService;

@Slf4j
@RestController
@Api(tags = "4. 회원 룩북")
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class MemberLookController {

	private final ResponseService responseService;
	private final MemberLookService memberLookService;
	private final int FAIL = -1;

	@PostMapping("/lookPage")
	@ApiOperation(value = "회원 단일 룩 페이지 생성", notes = "회원의 단일 룩 페이지를 생성합니다")
	public CommonResult registMemberLookPage(
			@ApiParam(value = "룩북 페이지 요청 객체") @ModelAttribute MemberLookPageReq lookPageReq
	) {
		try {
			memberLookService.registLookPage(lookPageReq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@GetMapping("/lookPage")
	@ApiOperation(value = "회원 단일 룩 페이지 조회", notes = "회원의 단일 룩 페이지를 조회합니다")
	public SingleResult<MemberLookPageRes> getMemberLookPage(
			@ApiParam(value = "룩 페이지 PK") @RequestParam Long lookSeq
	) {
		try {
			return responseService.getSingleResult(memberLookService.getLookPage(lookSeq));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/lookPages")
	@ApiOperation(value = "회원 룩 페이지 전체 조회", notes = "회원의 전체 룩 페이지를 조회합니다")
	public ListResult<MemberLookPageRes> getMemberLookPages(
			@ApiParam(value = "회원 이메일") @RequestParam String memberEmail
	) {
		try {
			return responseService.getListResult(memberLookService.getLookPages(memberEmail));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PutMapping("/lookPageInfo")
	@ApiOperation(value = "회원 룩 페이지 정보 수정", notes = "회원의 단일 룩 페이지 정보를 수정합니다.")
	public CommonResult updateMemberLookPage(
			@ApiParam(value = "룩 페이지 PK") @RequestParam Long lookSeq,
			@ApiParam(value = "룩 페이지 요청 객체") @ModelAttribute MemberLookPageReq memberLookPageReq
	) {
		try {
			memberLookService.updateLookPageInfo(lookSeq, memberLookPageReq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@PutMapping("/lookPageImage")
	@ApiOperation(value = "회원 룩 페이지 이미지 수정", notes = "회원의 단일 룩 페이지 이미지를 수정합니다.")
	public CommonResult updateLookPageImage(
			@ApiParam(value = "룩 페이지 PK") @RequestParam Long lookSeq,
			@ApiParam(value = "룩 페이지 요청 객체") @RequestBody MultipartFile requestImage
	) {
		try {
			memberLookService.updateLookPageImage(lookSeq, requestImage);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@DeleteMapping("/lookPageImage")
	@ApiOperation(value = "회원 룩 페이지 이미지 삭제", notes = "회원의 단일 룩 페이지를 삭제합니다.")
	public CommonResult deleteLookPage(
			@ApiParam(value = "룩 페이지 PK") @RequestParam Long lookSeq
	) {
		try {
			memberLookService.deleteLookPage(lookSeq);
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
