package sigma.Spring_backend.baseUtil.jwt.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import sigma.Spring_backend.baseUtil.jwt.authFilter.CustomAuthenticationFilter;
import sigma.Spring_backend.baseUtil.jwt.authFilter.CustomAuthorizationFilter;
import sigma.Spring_backend.socialSingin.LoginSuccessHandler;
import sigma.Spring_backend.socialSingin.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private final CustomOAuth2UserService customOAuth2UserService; // encoder를 빈으로 등록.
	private final CorsConfig corsConfig;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().mvcMatchers("/image/**"); // /image/** 있는 모든 파일들은 시큐리티 적용을 무시한다.
		web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()); // 정적인 리소스들에 대해서 시큐리티 적용 무시.
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();

		http
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and()
				.addFilter(corsConfig.corsFilter())
				.formLogin().disable()
				.httpBasic().disable()
				.addFilter(authenticationFilter())
				.addFilter(new CustomAuthorizationFilter(authenticationManager()))
				.authorizeRequests()
				.antMatchers("/v1/api/*")
				.access("hasRole('USER') or hasRole('ADMIN')")
				.anyRequest().permitAll() // 모든 요청에 대해서 허용하라.

				.and()
				.logout()
				.logoutSuccessUrl("/v1/api/login") // 로그아웃에 대해서 성공하면 "/"로 이동

				.and()
				.oauth2Login()
				.successHandler(new LoginSuccessHandler())
				.userInfoEndpoint()
				.userService(customOAuth2UserService);
		http.csrf().disable()
				.cors().disable()
				.headers().frameOptions().disable();
	}

	public UsernamePasswordAuthenticationFilter authenticationFilter() throws Exception {
		CustomAuthenticationFilter customAuthenticationFilter
				= new CustomAuthenticationFilter(authenticationManager());
		customAuthenticationFilter.setFilterProcessesUrl("/v1/api/login");
		return customAuthenticationFilter;
	}
}

