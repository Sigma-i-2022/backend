package sigma.Spring_backend.memberUtil.entity;

import lombok.*;
import sigma.Spring_backend.memberSignup.entity.AuthorizeEntity;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table(name = "member")
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

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    @JoinColumn(name = "auth_email", referencedColumnName = "email")
    private AuthorizeEntity authorizeUser;

    public MemberResponseDto toDto() {
        MemberResponseDto dto = new MemberResponseDto();
        dto.setUserId(userId);
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setSignupType(signupType);
        dto.setGender(gender);
        dto.setAge(age);
        return dto;
    }
}
