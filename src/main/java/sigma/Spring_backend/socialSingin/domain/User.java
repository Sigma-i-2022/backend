package sigma.Spring_backend.socialSingin.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = true)
    private String userId;

    @Column(nullable = false)
    private String email;

    @Builder
    public User(String email){
        this.email = email;
    }

    public User update(String userId){
        return this;
    }
}
