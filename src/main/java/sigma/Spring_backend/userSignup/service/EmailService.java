package sigma.Spring_backend.userSignup.service;

import sigma.Spring_backend.advice.exception.EmailException;
import sigma.Spring_backend.entity.base.CommonResult;
import sigma.Spring_backend.entity.base.SingleResult;
import sigma.Spring_backend.userSignup.dto.MemberSignupReqDto;

public interface EmailService {
	CommonResult sendAuthorizeCodeMail(String to) throws EmailException;

	CommonResult verifyEmail(String email, String code);

	SingleResult<Long> signUp(MemberSignupReqDto memberSignupReqDto);
}
