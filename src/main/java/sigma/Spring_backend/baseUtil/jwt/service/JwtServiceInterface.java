package sigma.Spring_backend.baseUtil.jwt.service;

import sigma.Spring_backend.memberUtil.entity.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface JwtServiceInterface {
	String createAccessToken(Member member);        // Access Token 생성

	String createRefreshToken();    // Refresh Token 생성

	boolean isExpiredToken(String token);    // 만료 토큰 검증

	boolean isExpiredInSevenDayToken(String token);        // 1주일 이내 만료 여부 검증

	boolean isValidHeader(HttpServletRequest request);

	boolean isValidToken(String token);        // 토큰 유효성 검증

	Member getMemberByToken(String token);

	void updateRefreshTokenOfUser(Member member, String token);        // member Refresh 토큰 업데이트

	void removeRefreshTokenOfUser(String token);
}
