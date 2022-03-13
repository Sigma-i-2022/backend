package sigma.Spring_backend.memberReport.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.memberReport.entity.MemberReport;

import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberReportReq {
	@ApiModelProperty(value = "회원 번호")
	private Long memberSeq;
	@ApiModelProperty(value = "회원 아이디")
	private String memberId;
	@ApiModelProperty(value = "리폿 제목")
	@Size(min = 8, max = 25, message = "8자 이상 25자 이내로 제목을 작성하세요.")
	private String reportTitle;
	@ApiModelProperty(value = "리폿 내용")
	@Size(min = 20, max = 500, message = "20자 이상, 500자 이내로 신고내용을 작성하세요.")
	private String reportContent;

	public MemberReport toEntity() {
		return MemberReport.builder()
				.memberId(memberId)
				.reportTitle(reportTitle)
				.reportContent(reportContent)
				.createDate(new DateConfig().getNowDate())
				.build();
	}
}
