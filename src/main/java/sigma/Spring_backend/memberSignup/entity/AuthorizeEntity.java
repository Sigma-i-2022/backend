package sigma.Spring_backend.memberSignup.entity;

import lombok.*;
import org.springframework.context.annotation.Bean;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authorizeEntity")
public class AuthorizeEntity {

	@Id
	@Column(name = "SEQ", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long seq;

	@Column(name = "auth_email", unique = true, nullable = false)
	String email;

	@Column(nullable = false)
	String code;

	@Column(nullable = false)
	boolean expired = false;

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "authorizeUser")
	private Member user;

	public void useCode() {
		if (!expired) expired = true;
	}
}
