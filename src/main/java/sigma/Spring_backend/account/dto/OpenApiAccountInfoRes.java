package sigma.Spring_backend.account.dto;

import lombok.*;

import javax.persistence.Column;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiAccountInfoRes {
	private Long crdiSeq;
	private String bankName;
	private String bankCode;
	private String birthDay;		// yyMMdd
	private String accountRealName;
	private String accountNum;
}
