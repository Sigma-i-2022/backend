package sigma.Spring_backend.clientLook.entity;

import lombok.*;
import sigma.Spring_backend.clientLook.dto.Keyword;
import sigma.Spring_backend.clientLook.dto.ClientLookPageRes;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ClientLookPage {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@Column(nullable = false)
	private String clientEmail;

	@Column(length = 300)
	@Builder.Default
	private String explanation = "";

	@Column
	private String imagePathUrl;

	@Column
	@Enumerated(EnumType.STRING)
	private Keyword keyword1;

	@Column
	@Enumerated(EnumType.STRING)
	private Keyword keyword2;

	@Column
	@Enumerated(EnumType.STRING)
	private Keyword keyword3;

	@Column
	private String topInfo;

	@Column
	private String bottomInfo;

	@Column
	private String shoeInfo;

	@Column
	private String registDate;

	@Column
	private String updateDate;

	@Column
	private String reportedYn;

	@Column
	private String reportContent;

	@Column
	private String activateYn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_SEQ")
	private Member member;

	public ClientLookPageRes toDto() {
		return ClientLookPageRes.builder()
				.lookPageSeq(seq)
				.explanation(explanation)
				.imagePathUrl(imagePathUrl)
				.keyword1(keyword1)
				.keyword2(keyword2)
				.keyword3(keyword3)
				.topInfo(topInfo)
				.bottomInfo(bottomInfo)
				.shoeInfo(shoeInfo)
				.updateDate(updateDate)
				.reportedYn(reportedYn)
				.reportContent(reportContent)
				.registDate(registDate)
				.build();
	}
}
