package sigma.Spring_backend.review.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.review.dto.ReviewRes;
import sigma.Spring_backend.review.dto.Sex;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq", nullable = false)
	private Long seq;

	@Column(nullable = false)
	private Long reservationSeq;

	@Column(nullable = false)
	private String reviewerEmail;

	@Column(nullable = false)
	private String coordinatorEmail;

	@Column(nullable = false)
	private String reviewerId;

	@Column(nullable = false)
	private String coordinatorId;

	@Column(nullable = false)
	private Integer star;

	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private Sex sex;

	@Column(nullable = false)
	private String height;

	@Column(nullable = false)
	private String weight;

	@Setter
	@Column(length = 300)
	private String content;

	@Column
	private String registDate;

	@Setter
	@Column
	private String activateYn;

	@Setter
	@Column
	private String reportedYn;

	@Setter
	@Column
	private String reportReason;

	@Setter
	@Column
	private String reportContent;

	@OneToOne
	@Builder.Default
	@JoinColumn(name="REPLY_SEQ")
	private Reply reply = null;

	public void setReply(Reply reply) {
		this.reply = reply;
		reply.setReview(this);
	}

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "COORDINATOR_SEQ")
	private Member coordinator;


	public ReviewRes toDto(String reviewerProfileImageUrl) {
		return ReviewRes.builder()
				.seq(seq)
				.reservationSeq(reservationSeq)
				.reviewerId(reviewerId)
				.reviewerProfileImagerUrl(reviewerProfileImageUrl)
				.coordinatorId(coordinatorId)
				.star(star)
				.sex(sex.name())
				.height(height)
				.weight(weight)
				.content(content)
				.registDate(registDate)
				.reportedYn(reportedYn)
				.reportReason(reportReason)
				.reportContent(reportContent)
				.replyRes(reply == null ? null : reply.toDto())
				.build();
	}
}
