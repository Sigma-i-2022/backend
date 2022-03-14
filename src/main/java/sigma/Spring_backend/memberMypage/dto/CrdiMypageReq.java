package sigma.Spring_backend.memberMypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrdiMypageReq {
	@NotBlank
	private String email;
	@NotBlank
	private String userId;
	@Size(max = 500, message = "500자 이하로 작성하세요.")
	private String intro;
	@NotBlank
	private String expertYN;
	@NotBlank
	private String sTag1;
	private String sTag2;
	private String sTag3;
	private MultipartFile profileImg;

	public CommonMypage toEntity(String profileImgUrl) {
		return CommonMypage.builder()
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
