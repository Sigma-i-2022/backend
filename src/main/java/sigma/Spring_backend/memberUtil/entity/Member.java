package sigma.Spring_backend.memberUtil.entity;

import lombok.*;
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
