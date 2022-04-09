package sigma.Spring_backend.submall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.submall.entity.Submall;

import java.util.UUID;

@Data
public class SubmallReqDto {
	@ApiModelProperty(value = "코디네이터 이메일", required = true)
	private String crdiEmail;
	@ApiModelProperty(value = "상호 명", required = true)
	private String companyName;
	@ApiModelProperty(value = "대표명", required = true)
	private String representativeName;
	@ApiModelProperty(value = "사업자 유형(개인/법인)", required = true)
	private String type;
	@ApiModelProperty(value = "사업자 등록 번호 (법인의 경우 입력)", example = "0000000000")
	private String businessNumber;
	private AccountDto account;

	public Submall toEntity(String subMallId) {
		if(type.equals("개인")) businessNumber = "";

		return Submall.builder()
				.crdiEmail(crdiEmail)
				.submallId(subMallId)
				.companyName(companyName)
				.representativeName(representativeName)
				.businessNumber(businessNumber)
				.type(type)
				.accountNumber(account.getAccountNumber())
				.bank(account.getBank())
				.build();
	}

	public TosspaymentSubmallReq toTossReq() {
		String curType;
		String curBusinessNumber;
		if(type.equals("개인")) {
			curBusinessNumber = "0000000000";
			curType = "INDIVIDUAL";
		} else {
			curType = "CORPORATE";
			curBusinessNumber = businessNumber;
		}

		return TosspaymentSubmallReq.builder()
				.subMallId(new DateConfig().getNowDate()+companyName)
				.companyName(companyName)
				.representativeName(representativeName)
				.businessNumber(curBusinessNumber)
				.type(curType)
				.account(account)
				.build();
	}
}
