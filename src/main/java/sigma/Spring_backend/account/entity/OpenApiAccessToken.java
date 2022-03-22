package sigma.Spring_backend.account.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiAccessToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq", nullable = false)
	private Long seq;

	@Setter
	@Column(nullable = false, length = 400)
	private String accessToken;		// 엑세스 토큰

	@Column(nullable = false)
	String tokenType;				// 토큰유형 Bearer

	@Setter
	@Column(nullable = false)
	String expireDate;				// 토큰 만료 날짜

	@Column(nullable = false)
	String scope;					// 토큰 권한 범위 oob

	@Column(nullable = false)
	String clientUseCode;			// 이용기관코드
}
