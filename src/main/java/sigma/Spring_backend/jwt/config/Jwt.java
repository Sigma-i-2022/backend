package sigma.Spring_backend.jwt.config;

public interface Jwt {
	String ACCESS = "AccessToken";
	String REFRESH = "RefreshToken";
	String EXCEPTION = "EXCEPTION";
	String USERNAME = "EMAIL";
	String ACCESS_TOKEN_HEADER = "Authorization";
	String REFRESH_TOKEN_HEADER = "Authorization-refresh";
	String TOKEN_PREFIX = "Bearer ";
	int ACCESS_TOKEN_EXPIRATION = (1000 * 60) * 30; // 30min
	long REFRESH_TOKEN_EXPIRATION = (1000 * 60 * 60 * 24) * 14; // 14day
}
