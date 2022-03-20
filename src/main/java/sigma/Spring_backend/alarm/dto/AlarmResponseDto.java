package sigma.Spring_backend.alarm.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AlarmResponseDto {
    private String email;
    private String alarmMsg;
    private String regDt;
}
