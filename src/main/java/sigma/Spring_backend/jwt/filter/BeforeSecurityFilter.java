package sigma.Spring_backend.jwt.filter;

import lombok.extern.slf4j.Slf4j;
import sigma.Spring_backend.jwt.config.Jwt;

import javax.servlet.*;
import java.io.IOException;

@Slf4j
public class BeforeSecurityFilter implements Filter {
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("<BEFORE SECURITY FILTER>");
		chain.doFilter(request, response);
	}
}
