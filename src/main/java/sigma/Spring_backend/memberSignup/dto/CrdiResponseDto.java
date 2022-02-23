package sigma.Spring_backend.memberSignup.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CrdiResponseDto {
    private String userId;
    private String email;
    private String career;
    private LocalDateTime regDt;
    private String joinYN;
}
