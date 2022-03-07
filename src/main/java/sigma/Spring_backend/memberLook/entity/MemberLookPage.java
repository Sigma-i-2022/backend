package sigma.Spring_backend.memberLook.entity;

import lombok.*;
import sigma.Spring_backend.memberLook.dto.Keyword;
import sigma.Spring_backend.memberLook.dto.MemberLookPageRes;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberLookPage {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@Column
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
	private String modelHeight;

	@Column
	private String modelWeight;

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
	private String activateYn;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_SEQ")
	private Member member;

	public Member getMember() {
		return this.member;
	}

	public void setMember(Member member) {
		this.member = member;
	}

	public MemberLookPageRes toDto() {
		return MemberLookPageRes.builder()
				.lookPageSeq(seq)
				.explanation(explanation)
				.imagePathUrl(imagePathUrl)
				.keyword1(keyword1)
				.keyword2(keyword2)
				.keyword3(keyword3)
				.modelHeight(modelHeight)
				.modelWeight(modelWeight)
				.topInfo(topInfo)
				.bottomInfo(bottomInfo)
				.shoeInfo(shoeInfo)
				.updateDate(updateDate)
				.registDate(registDate)
				.build();
	}
}
