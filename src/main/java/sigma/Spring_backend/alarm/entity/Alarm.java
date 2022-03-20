package sigma.Spring_backend.alarm.entity;

import lombok.*;
import sigma.Spring_backend.alarm.dto.AlarmResponseDto;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Alarm {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String alarmMsg;

    @Column
    private String registDate;

    public AlarmResponseDto toDto() {
        return AlarmResponseDto.builder()
                .email(email)
                .alarmMsg(alarmMsg)
                .regDt(registDate)
                .build();
    }

}
