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
