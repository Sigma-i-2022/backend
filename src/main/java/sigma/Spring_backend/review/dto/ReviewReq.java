package sigma.Spring_backend.review.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.review.entity.Review;

import javax.persistence.Column;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReviewReq {
	@ApiModelProperty(value = "예약 번호")
	private Long reservationSeq;
	@ApiModelProperty(value = "작성자 ID")
	private String reviewerId;
	@ApiModelProperty(value = "코디네이터 ID")
	private String coordinatorId;
	@ApiModelProperty(value = "평점")
	private Integer star;
	@ApiModelProperty(value = "성별")
	private Sex sex;
	@ApiModelProperty(value = "키")
	private String height;
	@ApiModelProperty(value = "몸무게")
	private String weight;
	@NotBlank
	@Size(min = 5, max = 300, message = "5자 이상 300자 이내로 작성해주세요.")
	@ApiModelProperty(value = "내용")
	private String content;

	public Review toEntity() {
		return Review.builder()
				.reservationSeq(reservationSeq)
				.reviewerId(reviewerId)
				.coordinatorId(coordinatorId)
				.star(star)
				.sex(sex)
				.height(height)
				.weight(weight)
				.content(content)
				.activateYn("Y")
				.reportedYn("N")
				.registDate(new DateConfig().getNowDate())
				.build();
	}
}
