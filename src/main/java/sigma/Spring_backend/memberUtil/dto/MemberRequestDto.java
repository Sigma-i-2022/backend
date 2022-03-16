package sigma.Spring_backend.memberUtil.dto;

import lombok.*;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.memberUtil.entity.Member;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    private String userId;
    private String email;
    private String password;
    private String signupType;

    public Member toEntity() {
        return Member.builder()
                .email(email)
                .userId(userId)
                .password(password)
                .signupType("E")
                .registDate(new DateConfig().getNowDate())
                .updateDate(new DateConfig().getNowDate())
                .activateYn("Y")
                .crdiYn("N")
                .build();
    }
}
