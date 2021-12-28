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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Gender gender;

    @Column(nullable = false)
    private int age;

    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .name(name)
                .email(email)
                .age(age)
                .gender(gender)
                .address(address)
                .build();
    }
}
