package sigma.Spring_backend.review.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRes {
	private Long seq;
	private Long reservationSeq;
	private String reviewerId;
	private String reviewerProfileImagerUrl;
	private String coordinatorId;
	private Integer star;
	private String sex;
	private String height;
	private String weight;
	private String content;
	private String reportedYn;
	private String reportReason;
	private String reportContent;
	private String registDate;
	private ReplyRes replyRes;
}
