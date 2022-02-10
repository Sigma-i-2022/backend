package sigma.Spring_backend.member;

import lombok.*;

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
