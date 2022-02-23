package sigma.Spring_backend.memberSignup.entity;

import lombok.*;
import sigma.Spring_backend.memberSignup.dto.CrdiResponseDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "join_crdi")
public class JoinCrdi {

    @Id
    @Column(name = "AUTH_SEQ", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    String email;

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String career;

    @Column(nullable = false)
    LocalDateTime regDt;

    @Column(nullable = false)
    String joinYN;

    public CrdiResponseDto toDto() {
        CrdiResponseDto dto = new CrdiResponseDto();
        dto.setEmail(email);
        dto.setUserId(userId);
        dto.setCareer(career);
        dto.setRegDt(regDt);
        dto.setJoinYN(joinYN);

        return dto;
    }
}
