package sigma.Spring_backend.memberSignup.entity;

import lombok.*;
import org.springframework.context.annotation.Bean;

import javax.persistence.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AuthorizeEntity {

	@Id
	@Column(name = "SEQ", nullable = false)
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long seq;

	@Column(nullable = false)
	String email;

	@Column(nullable = false)
	String code;

	@Column(nullable = false)
	boolean expired = false;

	public void useCode() {
		if (!expired) expired = true;
	}
}
