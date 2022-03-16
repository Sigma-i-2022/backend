package sigma.Spring_backend.memberReport.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.memberReport.dto.MemberReportReq;
import sigma.Spring_backend.memberReport.dto.MemberReportRes;
import sigma.Spring_backend.memberReport.service.MemberReportService;

@Api(tags = "B. 회원 신고")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/report")
public class MemberReportController {

	private final MemberReportService memberReportService;
	private final ResponseService responseService;

	@PostMapping
	@ApiOperation(
			value = "회원 신고",
			notes = "회원을 신고합니다."
	)
	public CommonResult reportMember(
			@ApiParam(value = "회원 신고 요청 폼", required = true) @ModelAttribute MemberReportReq memberReportReq
	) {
		try {
			memberReportService.reportMember(memberReportReq);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/member")
	@ApiOperation(
			value = "회원 별 신고 내역 조회",
			notes = "회원이 받은 모든 신고 내역을 조회합니다."
	)
	public ListResult<MemberReportRes> getAllReportedHistoryByMember(
			@ApiParam(value = "회원 번호", required = true) @RequestParam Long memberSeq
	) {
		try {
			return responseService.getListResult(memberReportService.getAllReportedHistoryByMember(memberSeq));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/all")
	@ApiOperation(
			value = "신고 내역 전체 조회",
			notes = "모든 신고 내역을 조회합니다."
	)
	public ListResult<MemberReportRes> getAllReportedHistory() {
		try {
			return responseService.getListResult(memberReportService.getAllReportedHistory());
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
