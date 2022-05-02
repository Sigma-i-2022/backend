package sigma.Spring_backend.memberMypage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrdiMypageReq {
	@ApiModelProperty(required = true)
	private String email;
	@ApiModelProperty(required = true)
	private String userId;
	@ApiModelProperty(required = true)
	private String intro;
	@ApiModelProperty(required = true)
	private String expertYN;
	private String tag1;
	private String tag2;
	private String tag3;

	public CommonMypage toEntity(String profileImgUrl) {
		return CommonMypage.builder()
				.email(email)
				.userId(userId)
				.intro(intro)
				.expertYN(expertYN)
				.sTag1(tag1)
				.sTag2(tag2)
				.sTag3(tag3)
				.profileImgUrl(profileImgUrl)
				.build();
	}
}
