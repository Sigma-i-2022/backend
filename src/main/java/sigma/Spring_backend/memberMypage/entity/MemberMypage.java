package sigma.Spring_backend.memberMypage.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member_mypage")
public class MemberMypage {
	@Id
	@Column(name = "MYPAGE_SEQ", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@Column(name = "mypage_email", unique = true, nullable = false)
	private String email;

	@Column(unique = true, nullable = false)
	private String userId;

	@Column
	@Builder.Default
	private String introduction = "";
}
