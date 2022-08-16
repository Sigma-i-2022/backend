package sigma.Spring_backend.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sigma.Spring_backend.jwt.filter.AfterSecurityFilter;
import sigma.Spring_backend.jwt.service.JwtService;

@Configuration
@RequiredArgsConstructor
public class FilterConfig {

	private final JwtService jwtService;

	//	Spring Security Filter 이후에 적용된다.
	@Bean
	public FilterRegistrationBean<AfterSecurityFilter> filter1() {
		FilterRegistrationBean<AfterSecurityFilter> bean = new FilterRegistrationBean<>(new AfterSecurityFilter(jwtService));
		bean.addUrlPatterns("/*");
		bean.setOrder(1);
		return bean;
	}
}
