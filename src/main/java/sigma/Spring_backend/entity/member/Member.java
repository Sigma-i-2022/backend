package sigma.Spring_backend.entity.member;

import lombok.*;
import sigma.Spring_backend.dto.member.MemberResponseDto;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String signupType;

    @Column
    private Gender gender;

    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .email(email)
                .gender(gender)
                .signupType(signupType)
                .build();
    }
}
