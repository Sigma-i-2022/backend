package sigma.Spring_backend.clientLook.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@AllArgsConstructor
public class ClientLookPageRes {
	@ApiModelProperty("룩 페이지 SEQ")
	private Long lookPageSeq;

	private String clientEmail;

	@ApiModelProperty("룩 페이지 설명")
	@Builder.Default
	private String explanation = "";

	@ApiModelProperty("이미지 경로")
	private String imagePathUrl;

	@ApiModelProperty("키워드 1")
	private String keyword1;

	@ApiModelProperty("키워드 2")
	private String keyword2;

	@ApiModelProperty("키워드 3")
	private String keyword3;

	@ApiModelProperty("상의 설명")
	private String topInfo;

	@ApiModelProperty("하의 설명")
	private String bottomInfo;

	@ApiModelProperty("신발 설명")
	private String shoeInfo;

	@ApiModelProperty("등록 날짜")
	private String registDate;

	@ApiModelProperty("수정 날짜")
	private String updateDate;

	private String reportedYn;

	private String reportContent;
}
