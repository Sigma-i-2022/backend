package sigma.Spring_backend.memberSignup.service;

import lombok.RequiredArgsConstructor;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sigma.Spring_backend.baseUtil.advice.BussinessExceptionMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;
import sigma.Spring_backend.memberSignup.repository.AuthorizeCodeRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberSignupService {

	private final JavaMailSender emailSender;
	private final AuthorizeCodeRepository authorizeCodeRepository;
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("${email.id}")
	private String sigmaEmail;

	public void sendAuthorizeCodeMail(String to) {
		verifyUserEmail(to);
		memberEmailDuplicateValidation(to);
		MimeMessage mail = createMessage(to);
		emailSender.send(mail);
	}

	@Transactional
	public void verifyAuthorizeCodeAndEmail(String email, String userInputCode)
	{
		Optional<AuthorizeMember> dbCode = authorizeCodeRepository.findByEmail(email);
		if (dbCode.isPresent() && dbCode.get().getCode().equals(userInputCode) && !dbCode.get().isExpired()) {
			dbCode.get().useCode();
		} else {
			throw new BussinessException(BussinessExceptionMessage.EMAIL_ERROR_FORMAT);
		}
	}

	@Transactional
	public void signUp(Map<String, String> userInfoMap) {

		String userId = userInfoMap.get("userId");
		String email = userInfoMap.get("email");
		String password = userInfoMap.get("password1");

		if (userId == null || email == null || password == null) {
			throw new BussinessException("회원정보가 제대로 입력되지 않았습니다.");
		}

		verifyUserInfo(userInfoMap);

		log.info(userId + ", " + email + ", " + password);

		if (memberRepository.findByEmail(email).isPresent()) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_DUPLICATE);
		} else if (!authorizeCodeRepository.findByEmail(email).get().isExpired()) {
			throw new BussinessException("인증되지 않은 이메일입니다. 인증된 이메일로 가입해주세요.");
		} else {
			try {
				password = passwordEncoder.encode(password);
				memberRepository.save(Member.builder()
						.email(email)
						.userId(userId)
						.password(password)
						.build());
			} catch (Exception e) {
				log.error(e.getMessage());
				throw new BussinessException("회원가입에 실패하였습니다.");
			}
		}
	}

	private void verifyUserInfo(Map<String, String> userInfoMap) {

		verifyUserId(userInfoMap.get("userId"));

		verifyUserEmail(userInfoMap.get("email"));

		verifyUserPassword(userInfoMap.get("password1"), userInfoMap.get("password2"));
	}

	private void verifyUserPassword(String password1, String password2) {
		Pattern passwordExpression = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}$");
		if (!passwordExpression.matcher(password1).matches()) {
			throw new BussinessException("비밀번호는 영문자와 특수문자를 포함하여 8자 이상으로 이뤄져야 합니다.");
		} else if (!password1.equals(password2)) {
			throw new BussinessException("입력한 비밀번호가 서로 다름니다.");
		}
	}

	private void verifyUserEmail(String email) {
		Pattern emailExpression = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
		if (!emailExpression.matcher(email).matches()) {
			throw new BussinessException(BussinessExceptionMessage.EMAIL_ERROR_FORMAT);
		}
	}

	private void verifyUserId(String userId) {
		// 시작은 영문으로만, '_'를 제외한 특수문자 안되며 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하
		Pattern nameExpression = Pattern.compile("^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$");
		if (!nameExpression.matcher(userId).matches()) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_USER_ID_FORMAT);
		}
	}

	private void memberEmailDuplicateValidation(String email) {
		if (memberRepository.findByEmail(email).isPresent()) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_DUPLICATE);
		}
	}

	@Transactional
	@Synchronized
	public MimeMessage createMessage(String toEmail) throws BussinessException {
		log.info("To : " + toEmail);

		if (!authorizeCodeRepository.findByEmail(toEmail).isPresent()) {
			authorizeCodeRepository.save(AuthorizeMember.builder()
					.email(toEmail)
					.code(createCode())
					.build());
		} else {
			AuthorizeMember authorize = authorizeCodeRepository.findByEmail(toEmail).get();
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
			msg.append(authorizeCodeRepository.findByEmail(toEmail).get().getCode()).append("</strong><div><br/> ");
			msg.append("</div>");

			mail.setText(msg.toString(), "utf-8", "html");
			mail.setFrom(new InternetAddress(sigmaEmail, "Sigma"));
		} catch (MessagingException | UnsupportedEncodingException e) {
			throw new BussinessException(BussinessExceptionMessage.EMAIL_ERROR_SEND);
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