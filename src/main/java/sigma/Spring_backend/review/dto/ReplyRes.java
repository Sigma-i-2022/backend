package sigma.Spring_backend.review.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyRes {

    private Long seq;
    private Long reviewSeq;
    private String crdiEmail;
    private String replyContent;

}
