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

	@ApiModelProperty(value = "회원 이메일", required = true)
	private String clientEmail;

	@ApiModelProperty(value = "룩 페이지 설명", required = true)
	@Builder.Default
	private String explanation = "";

	@ApiModelProperty(value = "키워드 1", required = true)
	@Enumerated(EnumType.STRING)
	private Keyword keyword1;

	@ApiModelProperty(value = "키워드 2", required = true)
	@Enumerated(EnumType.STRING)
	private Keyword keyword2;

	@ApiModelProperty(value = "키워드 3", required = true)
	@Enumerated(EnumType.STRING)
	private Keyword keyword3;

	@ApiModelProperty(value = "상의 설명", required = true)
	private String topInfo;

	@ApiModelProperty(value = "하의 설명", required = true)
	private String bottomInfo;

	@ApiModelProperty(value = "신발 설명", required = true)
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
