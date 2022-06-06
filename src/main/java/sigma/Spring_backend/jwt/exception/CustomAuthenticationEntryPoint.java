package sigma.Spring_backend.jwt.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import sigma.Spring_backend.jwt.config.Jwt;
import sigma.Spring_backend.jwt.service.JwtService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	private final JwtService jwtService;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		String errorMessage = (String) request.getAttribute(Jwt.EXCEPTION);
		log.error("오류 : " + errorMessage);
		log.error(">> "+authException.getMessage());
		jwtService.setResponseMessage(false, response, errorMessage);
	}
}
