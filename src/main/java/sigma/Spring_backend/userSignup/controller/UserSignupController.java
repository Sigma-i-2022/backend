package sigma.Spring_backend.userSignup.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.entity.base.CommonResult;
import sigma.Spring_backend.entity.base.SingleResult;
import sigma.Spring_backend.userSignup.dto.MemberSignupReqDto;
import sigma.Spring_backend.userSignup.service.EmailServiceImpl;

@Api(tags = "2. 이메일")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class UserSignupController {

	private final EmailServiceImpl emailService;

	@PostMapping("/email")
	@ApiOperation(value = "회원 가입 시 이메일 인증 코드 발송", notes = "회원의 이메일로 인증 코드 발송")
	public CommonResult emailConfirm(
			@ApiParam(value = "회원이 입력한 이메일", required = true)
			@RequestParam String email
	) {
		return emailService.sendAuthorizeCodeMail(email);
	}

	@PostMapping("/emailCodeVerify")
	@ApiOperation(value = "이메일 인증 코드 검증", notes = "회원이 입력한 인증코드를 검증합니다.")
	public CommonResult emailCodeVerify(
			@ApiParam(value = "회원이 입력한 인증 코드", required = true)
			@RequestParam String code,
			@ApiParam(value = "회원이 입력한 이메일", required = true)
			@RequestParam String email
	) {
		return emailService.verifyEmail(email, code);
	}

	@PostMapping("/signUp")
	@ApiOperation(value = "회원가입 요청", notes = "이메일, 비밀번호를 이용하여 회원가입")
	public SingleResult<Long> memberSignUp(
			@ApiParam(value = "회원가입 요청 폼", required = true)
			@RequestBody MemberSignupReqDto signupReqDto
	) {
		return emailService.signUp(signupReqDto);
	}
}
