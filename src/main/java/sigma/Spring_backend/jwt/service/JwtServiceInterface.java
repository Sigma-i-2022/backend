package sigma.Spring_backend.jwt.service;

import sigma.Spring_backend.jwt.dto.JwtError;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface JwtServiceInterface {
	String createAccessToken(String username);            // Access Token 생성

	String createRefreshToken();                        // Refresh Token 생성

	String extractRefreshToken(HttpServletRequest request);

	String extractAccessToken(HttpServletRequest request);

	boolean isNotExpiredToken(String token);                // 만료 토큰 검증

	boolean isExpiredInSevenDayTokenOrThrow(String token);        // 1주일 이내 만료 여부 검증

	boolean isValidHeaderOrThrow(HttpServletRequest request);

	JwtError checkValidTokenOrThrow(String token);                    // 토큰 유효성 검증

	Member getMemberByToken(String token);

	Member getMemberByUsername(String username);

	void setRefreshTokenToUser(Member member, String token);

	String updateRefreshTokenOfUser(Member member, String token);        // member Refresh 토큰 업데이트

	void removeRefreshTokenOfUser(String token);

	void setResponseMessage(boolean result, HttpServletResponse response, String message) throws IOException;
}
