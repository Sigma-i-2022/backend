package sigma.Spring_backend.memberUtil.entity;

import lombok.*;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;

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
    private Long seq;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String signupType;

    @Column
    private String gender;

    @Column
    private int age;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seq", referencedColumnName = "MYPAGE_SEQ")
    private MemberMypage mypage;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seq", referencedColumnName = "AUTH_SEQ")
    private AuthorizeMember authorizeUser;

    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .signupType(signupType)
                .gender(gender)
                .age(age)
                .build();
    }
}
