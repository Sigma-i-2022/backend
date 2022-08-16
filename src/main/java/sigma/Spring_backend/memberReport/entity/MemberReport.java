package sigma.Spring_backend.memberReport.entity;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.memberReport.dto.MemberReportRes;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Slf4j
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberReport {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq", nullable = false)
	private Long seq;

	@Column
	private String memberEmail;

	@Column
	private String reportTitle;

	@Column
	private String reportContent;

	@Column
	private String createDate;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_SEQ")
	private Member member;

	public MemberReportRes toDto() {
		return MemberReportRes.builder()
				.seq(seq)
				.memberSeq(member.getSeq())
				.memberEmail(memberEmail)
				.reportTitle(reportTitle)
				.reportContent(reportContent)
				.createDate(new DateConfig().getNowDate())
				.build();
	}
}
