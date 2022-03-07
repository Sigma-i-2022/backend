package sigma.Spring_backend.memberUtil.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberResponseDto {
    private Long userSeq;
    private String userId;
    private String email;
    private String password;
    private String signupType;
    private String activateYn;
    private String crdiYn;
    private String registDate;
    private String updateDate;
}
