package sigma.Spring_backend.member;

import lombok.*;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String signupType;

    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .name(name)
                .email(email)
                .signupType(signupType)
                .build();
    }
}
