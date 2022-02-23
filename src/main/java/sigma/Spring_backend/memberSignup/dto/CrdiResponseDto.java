package sigma.Spring_backend.memberSignup.dto;

import lombok.Data;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
public class CrdiResponseDto {
    private String userId;
    private String email;
    private String career;
    private String url1;
    private String url2;
    private String url3;
    private String url4;
    private String url5;
    private LocalDateTime regDt;
    private String joinYN;
}
