package sigma.Spring_backend.dto.member;

import lombok.*;
import sigma.Spring_backend.entity.member.Gender;
import sigma.Spring_backend.entity.member.Member;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberRequestDto {
    private String name;
    private String email;
    private String address;
    private Gender gender;
    private int age;

    public Member toEntity() {
        return Member.builder()
                .name(name)
                .age(age)
                .email(email)
                .gender(gender)
                .address(address)
                .build();
    }
}
