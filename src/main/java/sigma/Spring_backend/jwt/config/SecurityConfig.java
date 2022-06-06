package sigma.Spring_backend.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import sigma.Spring_backend.jwt.authFilter.CustomAuthenticationFilter;
import sigma.Spring_backend.jwt.authFilter.CustomAuthorizationFilter;
import sigma.Spring_backend.jwt.exception.CustomAccessDeniedHandler;
import sigma.Spring_backend.jwt.exception.CustomAuthenticationEntryPoint;
import sigma.Spring_backend.jwt.filter.BeforeSecurityFilter;
import sigma.Spring_backend.jwt.service.JwtService;
import sigma.Spring_backend.socialSingin.LoginSuccessHandler;
import sigma.Spring_backend.socialSingin.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final JwtService jwtService;
	private final CorsFilter corsFilter;
	private final CustomOAuth2UserService customOAuth2UserService; // encoder를 빈으로 등록.
	private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	private final CustomAccessDeniedHandler customAccessDeniedHandler;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers(
				"/image/**",
				"/v1/api/join/signUp",
				"/v1/api/join/email",
				"/v1/api/join/emailCode"
		); // /image/** 있는 모든 파일들은 시큐리티 적용을 무시한다.
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 정적인 리소스들에 대해서 시큐리티 적용 무시.
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.addFilterBefore(new BeforeSecurityFilter(), BasicAuthenticationFilter.class);
		http.csrf().disable();
		http
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(corsFilter)
				.formLogin().disable()
				.httpBasic().disable()
				.addFilter(authenticationFilter())
				.addFilter(new CustomAuthorizationFilter(authenticationManager(), jwtService))
				.authorizeRequests()
				.antMatchers("/v1/api/*")
				.access("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
				.anyRequest().permitAll() // 모든 요청에 대해서 허용하라.

				.and()
				.exceptionHandling()
				.authenticationEntryPoint(customAuthenticationEntryPoint)
				.accessDeniedHandler(customAccessDeniedHandler)

				.and()
				.logout()
				.logoutSuccessUrl("/v1/api/join/login") // 로그아웃에 대해서 성공하면 "/"로 이동

				.and()
				.oauth2Login()
				.successHandler(new LoginSuccessHandler())
				.userInfoEndpoint()
				.userService(customOAuth2UserService)
		;
	}

	public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
		CustomAuthenticationFilter customAuthenticationFilter
				= new CustomAuthenticationFilter(jwtService, authenticationManager());
		customAuthenticationFilter.setFilterProcessesUrl("/v1/api/join/login");
		return customAuthenticationFilter;
	}
}

