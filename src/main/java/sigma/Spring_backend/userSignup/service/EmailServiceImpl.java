package sigma.Spring_backend.userSignup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sigma.Spring_backend.advice.ErrorCode;
import sigma.Spring_backend.advice.exception.EmailException;
import sigma.Spring_backend.advice.exception.MemberEmailExistException;
import sigma.Spring_backend.entity.base.CommonResult;
import sigma.Spring_backend.entity.base.SingleResult;
import sigma.Spring_backend.repository.user.MemberRepository;
import sigma.Spring_backend.service.base.ResponseService;
import sigma.Spring_backend.service.member.MemberService;
import sigma.Spring_backend.userSignup.entity.AuthorizeEntity;
import sigma.Spring_backend.userSignup.dto.MemberSignupReqDto;
import sigma.Spring_backend.userSignup.repository.AuthorizeCodeRepo;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender emailSender;
	private final AuthorizeCodeRepo authorizeCodeRepo;
	private final ResponseService responseService;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${email.id}")
	private String id;

	@Override
	public CommonResult sendAuthorizeCodeMail(String to) throws EmailException {
		memberEmailDuplicateValidation(to);
		MimeMessage mail = createMessage(to);
		emailSender.send(mail);

		return responseService.getSuccessResult();
	}

	@Override
	@Transactional
	public CommonResult verifyEmail(String email, String code) {
		Optional<AuthorizeEntity> dbCode = authorizeCodeRepo.findByEmail(email);
		if (dbCode.isPresent() && dbCode.get().getCode().equals(code) && !dbCode.get().isExpired()) {
			dbCode.get().useCode();
			return responseService.getSuccessResult();
		}
		return responseService.getFailResult(
				ErrorCode.EmailException.getCode(),
				ErrorCode.EmailException.getMessage()
		);
	}

	@Override
	@Transactional
	public SingleResult<Long> signUp(MemberSignupReqDto memberSignupReqDto) {
		String email = memberSignupReqDto.getEmail();
		String password = memberSignupReqDto.getPassword();

		if (memberRepository.findByEmail(email).isPresent()) {
			throw new MemberEmailExistException();
		} else if (authorizeCodeRepo.findByEmail(email).isEmpty()) {
			throw new EmailException();
		} else if (!authorizeCodeRepo.findByEmail(email).get().isExpired()) {
			throw new EmailException();
		} else {
			memberSignupReqDto.setPassword(passwordEncoder.encode(password));
			memberSignupReqDto.setSignupType("email");
			Long id = memberRepository.save(memberSignupReqDto.toEntity()).getId();
			return responseService.getSingleResult(id);
		}
	}

	private void memberEmailDuplicateValidation(String email) {
		if (memberRepository.findByEmail(email).isPresent()) {
			throw new MemberEmailExistException();
		}
	}

	@Transactional
	public MimeMessage createMessage(String toEmail) throws EmailException {
		log.info("To : " + toEmail);

		if (authorizeCodeRepo.findByEmail(toEmail).isEmpty()) {
			authorizeCodeRepo.save(AuthorizeEntity.builder()
					.email(toEmail)
					.code(createCode())
					.build());
		} else {
			AuthorizeEntity authorize = authorizeCodeRepo.findByEmail(toEmail).get();
			authorize.setCode(createCode());
		}

		MimeMessage mail = emailSender.createMimeMessage();

		try {
			mail.addRecipients(Message.RecipientType.TO, toEmail);
			mail.setSubject("Sigma 회원가입 이메일 인증");

			StringBuilder msg = new StringBuilder();
			msg.append("<div style='margin:100px;'>");
			msg.append("<h1> 안녕하세요 Sigma입니다. </h1>");
			msg.append("<br>");
			msg.append("<p>아래 코드를 회원가입 창으로 돌아가 입력해주세요<p>");
			msg.append("<br>");
			msg.append("<p>감사합니다!<p>");
			msg.append("<br>");
			msg.append("<div align='center' style='border:1px solid black; font-family:verdana';>");
			msg.append("<h3 style='color:blue;'>회원가입 인증 코드입니다.</h3>");
			msg.append("<div style='font-size:130%'>");
			msg.append("CODE : <strong>");
			msg.append(authorizeCodeRepo.findByEmail(toEmail).get().getCode()).append("</strong><div><br/> ");
			msg.append("</div>");

			mail.setText(msg.toString(), "utf-8", "html");
			mail.setFrom(new InternetAddress(id, "Sigma"));
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new EmailException();
		}

		return mail;
	}

	private String createCode() {
		StringBuilder code = new StringBuilder();
		Random rand = new Random(System.currentTimeMillis());

		for (int i = 0; i < 8; i++) {
			int index = rand.nextInt(3);

			switch (index) {
				case 0:
					code.append((char) (rand.nextInt(26) + 97));
					break;
				case 1:
					code.append((char) (rand.nextInt(26) + 65));
					break;
				case 2:
					code.append(rand.nextInt(10));
					break;
			}
		}

		return code.toString();
	}
}
