package sigma.Spring_backend.memberLook.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MemberLookPageRes {
	@ApiModelProperty("룩 페이지 SEQ")
	private Long lookPageSeq;

	@ApiModelProperty("룩 페이지 설명")
	@Builder.Default
	private String explanation = "";

	@ApiModelProperty("이미지 경로")
	private String imagePathUrl;

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

	@ApiModelProperty("등록 날짜")
	private LocalDateTime registDate;

	@ApiModelProperty("수정 날짜")
	private LocalDateTime updateDate;
}
