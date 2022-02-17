package sigma.Spring_backend.member.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CrdiResponseDto {
    private String userId;
    private String email;
    private LocalDateTime regDt;
    private String joinYN;
}
