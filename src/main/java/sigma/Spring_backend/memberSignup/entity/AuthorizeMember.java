package sigma.Spring_backend.memberSignup.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuthorizeMember {
	@Id
	@Column(name = "AUTH_SEQ", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@Column(unique = true, nullable = false)
	String email;

	@Column(nullable = false)
	String code;

	@Column(nullable = false)
	@Builder.Default
	boolean expired = false;

	public void useCode() {
		if (!expired) expired = true;
	}
}
