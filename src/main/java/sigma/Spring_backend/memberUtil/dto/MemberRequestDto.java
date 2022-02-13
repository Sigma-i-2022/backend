package sigma.Spring_backend.memberUtil.dto;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;

@Data
public class MemberRequestDto {
    private String userId;
    private String email;
    private String password;
    private String signupType;
    private String gender;
    private int age;

    public Member toEntity() {
        return Member.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .signupType(signupType)
                .age(age)
                .gender(gender)
                .build();
    }
}
