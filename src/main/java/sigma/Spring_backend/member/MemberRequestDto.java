package sigma.Spring_backend.member;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String email;
    private String name;
    private String password;
    private String gender;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .name(name)
                .password(password)
                .build();
    }
}
