package sigma.Spring_backend.submall.dto;

import lombok.Data;

@Data
public class TosspaymentSubmallRes {
	private String subMallId;
	private String companyName;
	private String representativeName;
	private String type;
	private String businessNumber;
	private TosspaymentSubmallAccountDto account;
	private String metadata;
}
