package sigma.Spring_backend.memberSignup.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuthorizeMember {
	@Id
	@Column(unique = true, nullable = false)
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

	@OneToOne(mappedBy = "authorizeUser", orphanRemoval = true)
	private Member member;
}
