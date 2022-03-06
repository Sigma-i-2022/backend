package sigma.Spring_backend.memberUtil.dto;

import lombok.*;

import java.time.LocalDateTime;

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
    private LocalDateTime registDate;
    private LocalDateTime updateDate;
}
