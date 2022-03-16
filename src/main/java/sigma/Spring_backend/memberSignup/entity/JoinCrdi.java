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
    @Column(name = "JOIN_SEQ", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false, unique = true)
    String email;

    @Column(nullable = false)
    String userId;

    @Column(nullable = false, length = 1000)
    String career;

    @Column
    String url1;

    @Column
    String url2;

    @Column
    String url3;

    @Column
    String url4;

    @Column
    String url5;

    @Column(nullable = false)
    LocalDateTime regDt;

    @Column(nullable = false)
    String joinYN;

    @Column
    String confirmYN;

    public CrdiResponseDto toDto() {
        CrdiResponseDto dto = new CrdiResponseDto();
        dto.setEmail(email);
        dto.setUserId(userId);
        dto.setCareer(career);
        dto.setUrl1(url1);
        dto.setUrl2(url2);
        dto.setUrl3(url3);
        dto.setUrl4(url4);
        dto.setUrl5(url5);
        dto.setRegDt(regDt);
        dto.setJoinYN(joinYN);
        dto.setConfirmYN(confirmYN);


        return dto;
    }
}
