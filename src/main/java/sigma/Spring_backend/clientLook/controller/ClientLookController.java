package sigma.Spring_backend.clientLook.controller;

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
import sigma.Spring_backend.clientLook.dto.ClientLookPageReq;
import sigma.Spring_backend.clientLook.dto.ClientLookPageRes;
import sigma.Spring_backend.clientLook.service.ClientLookService;

@Slf4j
@RestController
@Api(tags = "04. 고객 룩북")
@RequiredArgsConstructor
@RequestMapping("/v1/api/lookPage")
public class ClientLookController {

	private final ResponseService responseService;
	private final ClientLookService clientLookService;
	private final int FAIL = -1;

	@PostMapping
	@ApiOperation(value = "고객 단일 룩 페이지 생성", notes = "고객의 단일 룩 페이지를 생성합니다")
	public CommonResult registClientLookPage(
			@ApiParam(value = "룩북 페이지 요청 객체") @ModelAttribute ClientLookPageReq lookPageReq,
			@ApiParam(value = "이미지 파일") @RequestParam MultipartFile imageFile
	) {
		try {
			clientLookService.registLookPage(lookPageReq, imageFile);
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
	@ApiOperation(value = "고객 단일 룩 페이지 조회", notes = "고객의 단일 룩 페이지를 조회합니다")
	public SingleResult<ClientLookPageRes> getClientLookPage(
			@ApiParam(value = "룩 페이지 PK") @RequestParam Long lookSeq
	) {
		try {
			return responseService.getSingleResult(clientLookService.getLookPage(lookSeq));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/all")
	@ApiOperation(value = "고객 룩 페이지 전체 조회", notes = "고객의 전체 룩 페이지를 조회합니다")
	public ListResult<ClientLookPageRes> getClientLookPages(
			@ApiParam(value = "고객 이메일") @RequestParam String email
	) {
		try {
			return responseService.getListResult(clientLookService.getLookPages(email));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PutMapping("/info")
	@ApiOperation(value = "고객 룩 페이지 정보 수정", notes = "고객의 단일 룩 페이지 정보를 수정합니다.")
	public CommonResult updateClientLookPage(
			@ApiParam(value = "룩 페이지 PK") @RequestParam Long lookSeq,
			@ApiParam(value = "룩 페이지 요청 객체") @ModelAttribute ClientLookPageReq clientLookPageReq
	) {
		try {
			clientLookService.updateLookPageInfo(lookSeq, clientLookPageReq);
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
	@ApiOperation(value = "고객 룩 페이지 이미지 수정", notes = "고객의 단일 룩 페이지 이미지를 수정합니다.")
	public CommonResult updateLookPageImage(
			@ApiParam(value = "룩 페이지 PK") @RequestParam Long lookSeq,
			@ApiParam(value = "룩 페이지 요청 객체") @RequestBody MultipartFile requestImage
	) {
		try {
			clientLookService.updateLookPageImage(lookSeq, requestImage);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@PostMapping("/image")
	@ApiOperation(value = "고객 룩 페이지 삭제", notes = "고객의 단일 룩 페이지를 삭제합니다.")
	public CommonResult deleteLookPage(
			@ApiParam(value = "룩 페이지 PK") @RequestParam Long lookSeq
	) {
		try {
			clientLookService.deleteLookPage(lookSeq);
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
			value = "고객 룩 페이지 신고",
			notes = "룩 페이지를 신고합니다."
	)
	public CommonResult reportLookPage(
			@ApiParam(value = "룩 페이지 PK", required = true) @RequestParam Long lookSeq,
			@ApiParam(value = "신고 이유") @RequestParam String reason
	) {
		try {
			clientLookService.reportLookPage(lookSeq, reason);
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
			value = "신고 받은 고객 룩 페이지 전체 조회",
			notes = "신고받은 룩 페이지를 모두 조회합니다."
	)
	public ListResult<ClientLookPageRes> getAllReportedLookPage() {
		try {
			return responseService.getListResult(clientLookService.getAllReportedPage());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}