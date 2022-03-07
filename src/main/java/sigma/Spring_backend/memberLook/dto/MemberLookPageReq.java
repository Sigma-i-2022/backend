package sigma.Spring_backend.memberLook.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.memberLook.entity.MemberLookPage;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberLookPageReq {

	@ApiModelProperty("회원 이메일")
	private String memberEmail;

	@ApiModelProperty("룩 페이지 설명")
	@Builder.Default
	private String explanation = "";

	@ApiModelProperty("이미지")
	private MultipartFile imageFile;

	@ApiModelProperty("키워드 1")
	@Enumerated(EnumType.STRING)
	private Keyword keyword1;

	@ApiModelProperty("키워드 2")
	@Enumerated(EnumType.STRING)
	private Keyword keyword2;

	@ApiModelProperty("키워드 3")
	@Enumerated(EnumType.STRING)
	private Keyword keyword3;

	@ApiModelProperty("모델 키")
	private String modelHeight;

	@ApiModelProperty("모델 몸무게")
	private String modelWeight;

	@ApiModelProperty("상의 설명")
	private String topInfo;

	@ApiModelProperty("하의 설명")
	private String bottomInfo;

	@ApiModelProperty("신발 설명")
	private String shoeInfo;

	public MemberLookPage toEntity(String imagePathUrl) {
		return MemberLookPage.builder()
				.explanation(this.explanation)
				.keyword1(this.keyword1)
				.keyword2(this.keyword2)
				.keyword3(this.keyword3)
				.modelHeight(this.modelHeight)
				.modelWeight(this.modelWeight)
				.topInfo(this.topInfo)
				.bottomInfo(this.bottomInfo)
				.shoeInfo(this.shoeInfo)
				.imagePathUrl(imagePathUrl)
				.updateDate(new DateConfig().getNowDate())
				.registDate(new DateConfig().getNowDate())
				.activateYn("Y")
				.build();
	}

}
