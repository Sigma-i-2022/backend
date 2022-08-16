package sigma.Spring_backend.jwt.authFilter;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import sigma.Spring_backend.jwt.auth.PrincipalUserDetails;
import sigma.Spring_backend.jwt.config.Jwt;
import sigma.Spring_backend.jwt.dto.JwtError;
import sigma.Spring_backend.jwt.exception.JwtException;
import sigma.Spring_backend.jwt.service.JwtService;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthorizationFilter extends BasicAuthenticationFilter {

	private final JwtService jwtService;

	public CustomAuthorizationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
		super(authenticationManager);
		this.jwtService = jwtService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("인가 / 권한 검증");

		try {
			// 올바르지 않은 헤더는 재로그인
			if (jwtService.isValidHeaderOrThrow(request)) {
				String refreshToken = jwtService.extractRefreshToken(request);
				String accessToken = jwtService.extractAccessToken(request);

				// 만료된 리프레쉬 토큰은 재로그인
				if (jwtService.isNotExpiredToken(refreshToken)) {

					Member memberByToken = jwtService.getMemberByToken(refreshToken);

					// 리프레쉬 토큰이 7일 이내 만료일 경우 새로 발급
					if (jwtService.isExpiredInSevenDayTokenOrThrow(refreshToken)) {
						log.info("[REFRESH TOKEN] > 리프레쉬 토큰 재발급");
						refreshToken = jwtService.updateRefreshTokenOfUser(memberByToken, refreshToken);
						jwtService.setResponseOfRefreshToken(response, refreshToken);
					}

					// 액세스 토큰이 만료된 경우 새로 발급
					if (jwtService.checkValidTokenOrThrow(accessToken) == JwtError.JWT_ACCESS_EXPIRED) {
						log.info("[ACCESS TOKEN] > 액세스 토큰 재발급");
						String reissuedAccessToken = jwtService.createAccessToken(memberByToken.getEmail());
						jwtService.setResponseOfAccessToken(response, reissuedAccessToken);
					}

					PrincipalUserDetails principal = new PrincipalUserDetails(memberByToken);
					Authentication authentication = new UsernamePasswordAuthenticationToken(
							principal, null, principal.getAuthorities()
					);

					SecurityContextHolder.getContext().setAuthentication(authentication);
				}
			}
			log.info("[LOGIN] > 재 로그인 필요");
		} catch (AuthenticationException jwtException) {
			log.error("=================================인가 에러=================================");
			log.error(jwtException.getMessage());
			log.error("=================================인가 에러=================================");
			request.setAttribute(Jwt.EXCEPTION, jwtException.getMessage());
		} catch (Exception e) {
			log.error("=================================미정의 에러=================================");
			log.error(e.getMessage());
			log.error("=================================미정의 에러=================================");
			e.printStackTrace();
			request.setAttribute(Jwt.EXCEPTION, e.getMessage());
		}

		chain.doFilter(request, response);
	}
}
