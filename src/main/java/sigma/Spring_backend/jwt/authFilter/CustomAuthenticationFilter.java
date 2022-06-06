package sigma.Spring_backend.jwt.authFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.jwt.auth.PrincipalUserDetails;
import sigma.Spring_backend.jwt.service.JwtService;
import sigma.Spring_backend.memberSignup.dto.LoginDto;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final JwtService jwtService;
	private final AuthenticationManager authenticationManager;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
		log.info("인증 시도");
		ObjectMapper om = new ObjectMapper();

		try {
			LoginDto loginDto = om.readValue(request.getInputStream(), LoginDto.class);

			UsernamePasswordAuthenticationToken authenticationToken =
					new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

			Authentication authenticate = authenticationManager.authenticate(authenticationToken);
			return authenticate;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
		log.info("인증 성공");
		PrincipalUserDetails principal = (PrincipalUserDetails) authResult.getPrincipal();

		String accessToken = jwtService.createAccessToken(principal.getUsername());
		String refreshToken = jwtService.createRefreshToken();

		Member memberByUsername = jwtService.getMemberByUsername(principal.getUsername());
		jwtService.updateRefreshTokenOfUser(memberByUsername, refreshToken);

		jwtService.setResponseOfAccessToken(response, accessToken);
		jwtService.setResponseOfRefreshToken(response, refreshToken);
		jwtService.setResponseMessage(true, response, "로그인 성공");
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
		log.info("인증 실패");
		ExMessage failMessage = failed.getMessage().equals(ExMessage.MEMBER_ERROR_NOT_FOUND_ENG.getMessage()) ?
				ExMessage.MEMBER_ERROR_NOT_FOUND :
				ExMessage.MEMBER_ERROR_PASSWORD;
		jwtService.setResponseMessage(false, response, "로그인 실패" + ": " + failMessage);
	}
}
