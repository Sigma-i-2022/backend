package sigma.Spring_backend.review.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false)
    private Long seq;

    @Column(length = 300)
    private String replyContent;

    @Column
    private String crdiEmail;

    @Setter
    @OneToOne
    @JoinColumn(name="REVIEW_SEQ")
    private Review review;


}
