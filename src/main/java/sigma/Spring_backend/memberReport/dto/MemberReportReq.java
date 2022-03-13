package sigma.Spring_backend.memberReport.dto;

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
	private Long memberSeq;
	private String memberId;
	@Size(min = 8, max = 25, message = "8자 이상 25자 이내로 제목을 작성하세요.")
	private String reportTitle;
	@Size(min = 20, max = 500, message = "20자 이상, 500자 이내로 신고내용을 작성하세요.")
	private String reportContent;

	public MemberReport toEntity() {
		return MemberReport.builder()
				.memberSeq(memberSeq)
				.memberId(memberId)
				.reportTitle(reportTitle)
				.reportContent(reportContent)
				.createDate(new DateConfig().getNowDate())
				.build();
	}
}
