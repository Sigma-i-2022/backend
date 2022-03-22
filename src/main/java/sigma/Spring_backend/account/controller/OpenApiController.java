package sigma.Spring_backend.account.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.account.dto.BANK_CODE;
import sigma.Spring_backend.account.dto.OpenApiAccountInfoRes;
import sigma.Spring_backend.account.service.OpenApiService;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;

@Api(tags = "14. 금융결제원 OpenAPI")
@RestController
@RequestMapping("/v1/api/openapi")
@RequiredArgsConstructor
public class OpenApiController {

	private final OpenApiService openApiService;
	private final ResponseService responseService;
	private final int FAIL = -1;

	@PostMapping("/token")
	@ApiOperation(value = "금융결제원 AccessToken 발급", notes = "만료되었다면 액세스 토큰을 새로 발급합니다.")
	public CommonResult requestOpenApiAccessToken() {

		try {
			openApiService.requestOpenApiAccessToken();
			return responseService.getSuccessResult();
		} catch (Exception e) {
			return responseService.getFailResult(
					FAIL,
					e.getMessage()
			);
		}
	}

	@PostMapping("/realname")
	@ApiOperation(value = "계좌번호 실명 조회 후 등록", notes = "해당 계좌번호와 예금주명이 일치하는지 확인 후 등록합니다.")
	public SingleResult<Boolean> requestMatchAccountRealName(
			@ApiParam(value = "코디 번호", required = true) @RequestParam Long crdiSeq,
			@ApiParam(value = "은행 코드", required = true) @RequestParam BANK_CODE bankCode,
			@ApiParam(value = "계좌 번호", required = true) @RequestParam String bankAccount,
			@ApiParam(value = "예금주 성함", required = true) @RequestParam String realName,
			@ApiParam(value = "예금주 생년월일 yyMMdd", required = true) @RequestParam String birthday
	) {
		try {
			boolean result = openApiService.requestMatchAccountRealName(crdiSeq, bankCode.getBankCode(), bankAccount, realName, birthday);
			return responseService.getSingleResult(result);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/account")
	@ApiOperation(value = "코디네이터 등록 계좌 정보 조회", notes = "코디네이터가 등록한 계좌 정보를 조회합니다.")
	public SingleResult<OpenApiAccountInfoRes> getCrdiAccountInfo(
			@ApiParam(value = "코디 번호", required = true) @RequestParam Long crdiSeq
	) {
		try {
			return responseService.getSingleResult(openApiService.getCrdiAccountInfo(crdiSeq));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
