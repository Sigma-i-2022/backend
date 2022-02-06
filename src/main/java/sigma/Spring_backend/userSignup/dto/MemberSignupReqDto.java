package sigma.Spring_backend.userSignup.dto;

import lombok.*;
import sigma.Spring_backend.entity.member.Member;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberSignupReqDto {
	private String email;
	private String password;
	private String signupType;

	public Member toEntity() {
		return Member.builder()
				.email(email)
				.password(password)
				.signupType(signupType)
				.build();
	}
}
