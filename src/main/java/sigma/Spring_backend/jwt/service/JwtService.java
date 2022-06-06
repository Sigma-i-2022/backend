package sigma.Spring_backend.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.jwt.config.Jwt;
import sigma.Spring_backend.jwt.dto.JwtError;
import sigma.Spring_backend.jwt.exception.JwtException;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService implements JwtServiceInterface {

	@Value("${jwt.secret-key}")
	private String SECRET_KEY;

	private final MemberRepository memberRepository;

	@Override
	public String createAccessToken(String username) {
		return JWT.create()
				.withSubject(Jwt.ACCESS)
				.withClaim(Jwt.USERNAME, username)
				.withExpiresAt(new Date(System.currentTimeMillis() + Jwt.ACCESS_TOKEN_EXPIRATION))
				.sign(Algorithm.HMAC512(SECRET_KEY));
	}

	@Override
	public String createRefreshToken() {
		return JWT.create()
				.withSubject(Jwt.REFRESH)
				.withExpiresAt(new Date(System.currentTimeMillis() + Jwt.REFRESH_TOKEN_EXPIRATION))
				.sign(Algorithm.HMAC512(SECRET_KEY));
	}

	@Override
	public boolean isExpiredToken(String token) {
		try {
			JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
		} catch (TokenExpiredException expiredException) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isExpiredInSevenDayToken(String token) {
		try {
			Date expiresAt = JWT.require(Algorithm.HMAC512(SECRET_KEY))
					.build()
					.verify(token)
					.getExpiresAt();

			Date current = new Date(System.currentTimeMillis());
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(current);
			calendar.add(Calendar.DATE, 7);

			Date after7dayFromToday = calendar.getTime();
			if (expiresAt.before(after7dayFromToday)) {
				return true;
			}
		} catch (TokenExpiredException e) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isValidHeader(HttpServletRequest request) {

		String accessToken = request.getHeader(Jwt.ACCESS_TOKEN_HEADER);
		String refreshToken = request.getHeader(Jwt.REFRESH_TOKEN_HEADER);

		if (accessToken != null && refreshToken != null) {
			if (accessToken.startsWith(Jwt.TOKEN_PREFIX) && refreshToken.startsWith(Jwt.TOKEN_PREFIX)) {
				return true;
			}
		}

//		throw new JwtException(JwtError.JWT_HEADER_NOT_VALID);
//		request.setAttribute(Jwt.EXCEPTION, JwtError.JWT_HEADER_NOT_VALID.getMessage());
		return false;
	}

	@Override
	public boolean isValidToken(String token) {
		try {
			JWT.require(Algorithm.HMAC512(SECRET_KEY))
					.build()
					.verify(token);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public Member getMemberByToken(String token) {
		return memberRepository.findByRefreshTokenFJ(token)
				.orElseThrow(() -> new JwtException(JwtError.JWT_MEMBER_NOT_FOUND));
	}

	@Override
	public Member getMemberByUsername(String username) {
		return memberRepository.findByEmailFJ(username)
				.orElseThrow(() -> new JwtException(JwtError.JWT_MEMBER_NOT_FOUND));
	}

	@Override
	@Transactional
	public void updateRefreshTokenOfUser(Member member, String token) {
		memberRepository.findByEmailFJ(member.getEmail())
				.orElseThrow(() -> new JwtException(JwtError.JWT_MEMBER_NOT_FOUND))
				.setRefreshToken(token);
	}

	@Override
	@Transactional
	public void removeRefreshTokenOfUser(String token) {
		memberRepository.findByRefreshTokenFJ(token)
				.orElseThrow(() -> new JwtException(JwtError.JWT_MEMBER_NOT_FOUND))
				.setRefreshToken(null);
	}

	public void setResponseOfAccessToken(HttpServletResponse response, String token) {
		response.addHeader(Jwt.ACCESS_TOKEN_HEADER, Jwt.TOKEN_PREFIX + token);
	}

	public void setResponseOfRefreshToken(HttpServletResponse response, String token) {
		response.addHeader(Jwt.REFRESH_TOKEN_HEADER, Jwt.TOKEN_PREFIX + token);
	}

	@Override
	public void setResponseMessage(boolean result, HttpServletResponse response, String message) throws IOException {
		JSONObject object = new JSONObject();
		response.setContentType("application/json;charset=UTF-8");
		if (result) {
			response.setStatus(HttpServletResponse.SC_OK);
			object.put("success", true);
			object.put("code", 1);
			object.put("message", message);
		} else {
			response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
			object.put("success", false);
			object.put("code", -1);
			object.put("message", message);
		}
		response.getWriter().print(object);
	}
}
