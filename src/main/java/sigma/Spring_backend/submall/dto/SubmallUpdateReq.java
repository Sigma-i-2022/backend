package sigma.Spring_backend.submall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class SubmallUpdateReq {
	@ApiModelProperty(value = "상호 명", required = true)
	private String companyName;
	@ApiModelProperty(value = "대표명", required = true)
	private String representativeName;
	@ApiModelProperty(value = "사업자 등록 번호 (법인의 경우 입력)", example = "0000000000")
	private String businessNumber;
	private SubmallAccountDto account;

	public TosspaymentSubmallUpdateReq toTossReq() {
		if (businessNumber == null || businessNumber.length() == 0) {
			businessNumber = "0000000000";
		}

		return TosspaymentSubmallUpdateReq.builder()
				.companyName(companyName)
				.representativeName(representativeName)
				.businessNumber(businessNumber)
				.account(account.toTossAccount())
				.build();
	}
}
