package sigma.Spring_backend.member.entity;

import lombok.*;
import sigma.Spring_backend.member.dto.CrdiResponseDto;
import sigma.Spring_backend.memberUtil.entity.Member;

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

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String userId;

    @Column(nullable = false)
    String career;

    @Column(nullable = false)
    LocalDateTime regDt;

    @Column(nullable = false)
    String joinYN;

    @ManyToOne
    @JoinColumn(name = "seq")
    private Member member;

    public CrdiResponseDto toDto() {
        CrdiResponseDto dto = new CrdiResponseDto();
        dto.setEmail(email);
        dto.setUserId(userId);
        dto.setRegDt(regDt);
        dto.setJoinYN(joinYN);

        return dto;
    }
}
