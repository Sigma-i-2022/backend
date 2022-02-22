package sigma.Spring_backend.memberSignup.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.advice.BussinessExceptionMessage;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.memberSignup.dto.CrdiResponseDto;
import sigma.Spring_backend.memberSignup.service.MemberSignupService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "2. 회원가입")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class MemberSignupController {

	private final MemberSignupService memberSignupService;
	private final ResponseService responseService;

	@PostMapping("/email")
	@ApiOperation(value = "회원 가입 시 이메일 인증 코드 발송", notes = "회원의 이메일로 인증 코드 발송")
	public CommonResult emailConfirm(
			@ApiParam(value = "회원이 입력한 이메일", required = true)
			@RequestParam String email
	) {
		try {
			memberSignupService.sendAuthorizeCodeMail(email);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			log.error(e.getMessage());
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@PostMapping("/emailCode")
	@ApiOperation(value = "이메일 인증 코드 검증", notes = "회원이 입력한 인증코드를 검증합니다.")
	public CommonResult emailCodeVerify(
			@ApiParam(value = "회원이 입력한 인증 코드", required = true)
			@RequestParam String code,
			@ApiParam(value = "회원이 입력한 이메일", required = true)
			@RequestParam String email
	) {
		try {
			memberSignupService.verifyAuthorizeCodeAndEmail(email, code);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			return responseService.getFailResult(
					-1,
					BussinessExceptionMessage.EMAIL_ERROR_CODE.getMessage()
			);
		}
	}

	@PostMapping("/signUp")
	@ApiOperation(value = "회원가입 요청", notes = "이메일, 비밀번호를 이용하여 회원가입")
	public CommonResult memberSignUp(
			@ApiParam(value = "회원 아이디", required = true) @RequestParam(name = "userId") String name,
			@ApiParam(value = "회원 이메일", required = true) @RequestParam(name = "email") String email,
			@ApiParam(value = "회원 패스워드1", required = true) @RequestParam(name = "password1") String password1,
			@ApiParam(value = "회원 패스워드2", required = true) @RequestParam(name = "password2") String password2
	) {
		Map<String, String> userInfoMap = new HashMap<>();
		userInfoMap.put("userId", name);
		userInfoMap.put("email", email);
		userInfoMap.put("password1", password1);
		userInfoMap.put("password2", password2);


		try {
			memberSignupService.signUp(userInfoMap);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			log.error(e.getMessage());
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@PostMapping("/login")
	@ApiOperation(value = "로그인", notes = "이메일, 비밀번호를 이용하여 로그인")
	public CommonResult memberSignIn(
			@ApiParam(value = "회원 이메일", required = true) @RequestParam(name = "email") String email,
			@ApiParam(value = "회원 패스워드", required = true) @RequestParam(name = "password") String password
	) {
		Map<String, String> userInfoMap = new HashMap<>();
		userInfoMap.put("email", email);
		userInfoMap.put("password", password);

		try {
			log.info(email+password);
			memberSignupService.signIn(userInfoMap);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			log.error(e.getMessage());
			return responseService.getFailResult(
					-1,
					e.getMessage()
			);
		}
	}

	@PostMapping("/join")
	@ApiOperation(value = "코디 자격 신청", notes = "코디네이터 신청")
	public SingleResult<CrdiResponseDto> crdiJoin(
			@ApiParam(value = "코디 이메일", required = true) @RequestParam(name = "email") String email,
			@ApiParam(value = "코디 아이디", required = true) @RequestParam(name = "userId") String userId,
			@ApiParam(value = "코디 경력사항", required = true) @RequestParam(name = "career") String career
	) {
		Map<String, String> crdiInfoMap = new HashMap<>();
		crdiInfoMap.put("email", email);
		crdiInfoMap.put("userId", userId);
		crdiInfoMap.put("career", career);

		return responseService.getSingleResult(memberSignupService.crdiJoin(crdiInfoMap));
	}

	@GetMapping("/joinState")
	@ApiOperation(value = "코디 신청 여부", notes = "코디네이터 신청여부")
	public SingleResult<CrdiResponseDto> crdiJoin(
			@ApiParam(value = "코디 이메일", required = true) @RequestParam(name = "email") String email
	) {
		Map<String, String> crdiInfoMap = new HashMap<>();
		crdiInfoMap.put("email", email);

		CrdiResponseDto byEmail = memberSignupService.findCrdiJoinYn(email);
		return responseService.getSingleResult(byEmail);
	}

}
