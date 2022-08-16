package sigma.Spring_backend.review.entity;

import lombok.*;
import sigma.Spring_backend.review.dto.ReplyRes;
import sigma.Spring_backend.review.dto.ReviewRes;

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

    @Column
    private String activeYN;

    @Setter
    @OneToOne(mappedBy = "reply")
    private Review review;

    public ReplyRes toDto() {
        return ReplyRes.builder()
                .seq(seq)
                .replyContent(replyContent)
                .crdiEmail(crdiEmail)
                .reviewSeq(review.getSeq())
                .build();
    }


}
