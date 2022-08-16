package sigma.Spring_backend.memberReport.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberReportRes {
	private Long seq;
	private Long memberSeq;
	private String memberEmail;
	private String reportTitle;
	private String reportContent;
	private String createDate;
}
