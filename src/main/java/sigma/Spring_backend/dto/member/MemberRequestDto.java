package sigma.Spring_backend.dto.member;

import lombok.*;
import sigma.Spring_backend.entity.member.Gender;
import sigma.Spring_backend.entity.member.Member;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String email;
    private String password;
    private Gender gender;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .gender(gender)
                .password(password)
                .build();
    }
}
