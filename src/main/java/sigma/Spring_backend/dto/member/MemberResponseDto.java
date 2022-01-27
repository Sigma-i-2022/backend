package sigma.Spring_backend.dto.member;

import lombok.*;
import sigma.Spring_backend.entity.member.Gender;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private String name;
    private String email;
    private String address;
    private Gender gender;
    private int age;
}
