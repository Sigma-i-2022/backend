package sigma.Spring_backend.dto.member;

import lombok.*;
import sigma.Spring_backend.entity.member.Gender;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String email;
    private String password;
    private String signupType;
    private Gender gender;
}
