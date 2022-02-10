package sigma.Spring_backend.member;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String name;
    private String email;
    private String password;
    private String signupType;
}
