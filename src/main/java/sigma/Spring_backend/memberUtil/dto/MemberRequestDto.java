package sigma.Spring_backend.memberUtil.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.memberUtil.entity.Member;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberRequestDto {
    @ApiModelProperty(required = true)
    private String userId;
    @ApiModelProperty(required = true)
    private String email;
    @ApiModelProperty(required = true)
    private String password;
    @ApiModelProperty(required = true)
    private String signupType;

    public Member toEntity(String password, String role) {
        return Member.builder()
                .email(email)
                .userId(userId)
                .password(password)
                .role(role)
                .registDate(new DateConfig().getNowDate())
                .updateDate(new DateConfig().getNowDate())
                .activateYn("Y")
                .reportedYn("N")
                .signupType("E")
                .crdiYn("N")
                .build();
    }
}
