package sigma.Spring_backend.memberMypage.entity;

import lombok.*;
import sigma.Spring_backend.memberMypage.dto.ClientMypageRes;
import sigma.Spring_backend.memberMypage.dto.CrdiMypageRes;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonMypage {
	@Id
	@Column(unique = true, nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(unique = true, nullable = false)
	private String userId;

	@Column(length = 500)
	@Builder.Default
	private String intro = "";

	@Column
	@Builder.Default
	private String profileImgUrl = "";

	@Column
	private String expertYN;

	@Column
	private String sTag1;

	@Column
	private String sTag2;

	@Column
	private String sTag3;

	public ClientMypageRes toClientDto() {
		return ClientMypageRes.builder()
				.seq(seq)
				.email(email)
				.userId(userId)
				.intro(intro)
				.profileImgUrl(profileImgUrl)
				.build();
	}

	public CrdiMypageRes toCrdiDto() {
		return CrdiMypageRes.builder()
				.seq(seq)
				.email(email)
				.userId(userId)
				.intro(intro)
				.expertYN(expertYN)
				.sTag1(sTag1)
				.sTag2(sTag2)
				.sTag3(sTag3)
				.profileImgUrl(profileImgUrl)
				.build();
	}
}
