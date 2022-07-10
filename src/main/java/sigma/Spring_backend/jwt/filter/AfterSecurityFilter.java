package sigma.Spring_backend.jwt.filter;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import sigma.Spring_backend.jwt.config.Jwt;
import sigma.Spring_backend.jwt.service.JwtService;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class AfterSecurityFilter implements Filter {

	private final JwtService jwtService;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("<AFTER SECURITY FILTER>");
		String exceptionMessage = (String) request.getAttribute(Jwt.EXCEPTION);
		if (exceptionMessage != null) {
			jwtService.setResponseMessage(false, (HttpServletResponse) response, exceptionMessage);
		} else {
			chain.doFilter(request, response);
		}
	}
}
