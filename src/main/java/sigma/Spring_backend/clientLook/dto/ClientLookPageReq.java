package sigma.Spring_backend.clientLook.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.clientLook.entity.ClientLookPage;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientLookPageReq {

	@ApiModelProperty("회원 이메일")
	private String clientEmail;

	@ApiModelProperty("룩 페이지 설명")
	@Builder.Default
	private String explanation = "";

	@ApiModelProperty("키워드 1")
	@Enumerated(EnumType.STRING)
	private Keyword keyword1;

	@ApiModelProperty("키워드 2")
	@Enumerated(EnumType.STRING)
	private Keyword keyword2;

	@ApiModelProperty("키워드 3")
	@Enumerated(EnumType.STRING)
	private Keyword keyword3;

	@ApiModelProperty("상의 설명")
	private String topInfo;

	@ApiModelProperty("하의 설명")
	private String bottomInfo;

	@ApiModelProperty("신발 설명")
	private String shoeInfo;

	public ClientLookPage toEntity(String imagePathUrl) {
		return ClientLookPage.builder()
				.explanation(this.explanation)
				.clientEmail(clientEmail)
				.keyword1(this.keyword1)
				.keyword2(this.keyword2)
				.keyword3(this.keyword3)
				.topInfo(this.topInfo)
				.bottomInfo(this.bottomInfo)
				.shoeInfo(this.shoeInfo)
				.imagePathUrl(imagePathUrl)
				.updateDate(new DateConfig().getNowDate())
				.registDate(new DateConfig().getNowDate())
				.activateYn("Y")
				.reportedYn("N")
				.build();
	}

}
