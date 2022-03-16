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
@RequestMapping("/v1/api/lookPage")
public class MemberLookController {

	private final ResponseService responseService;
	private final MemberLookService memberLookService;
	private final int FAIL = -1;

	@PostMapping
	@ApiOperation(value = "회원 단일 룩 페이지 생성", notes = "회원의 단일 룩 페이지를 생성합니다")
	public CommonResult registMemberLookPage(
			@ApiParam(value = "룩북 페이지 요청 객체") @ModelAttribute MemberLookPageReq lookPageReq,
			@ApiParam(value = "이미지 파일") @RequestParam MultipartFile imageFile
	) {
		try {
			memberLookService.registLookPage(lookPageReq, imageFile);
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

	@GetMapping("/all")
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

	@PutMapping("/info")
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

	@PutMapping("/image")
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

	@DeleteMapping("/image")
	@ApiOperation(value = "회원 룩 페이지 삭제", notes = "회원의 단일 룩 페이지를 삭제합니다.")
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

	@PostMapping("/report")
	@ApiOperation(
			value = "회원 룩 페이지 신고",
			notes = "룩 페이지를 신고합니다."
	)
	public CommonResult reportLookPage(
			@ApiParam(value = "룩 페이지 PK", required = true) @RequestParam Long lookSeq,
			@ApiParam(value = "신고 이유") @RequestParam String reason
	) {
		try {
			memberLookService.reportLookPage(lookSeq, reason);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@GetMapping("/report/all")
	@ApiOperation(
			value = "신고 받은 회원 룩 페이지 전체 조회",
			notes = "신고받은 룩 페이지를 모두 조회합니다."
	)
	public ListResult<MemberLookPageRes> getAllReportedLookPage() {
		try {
			return responseService.getListResult(memberLookService.getAllReportedPage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
