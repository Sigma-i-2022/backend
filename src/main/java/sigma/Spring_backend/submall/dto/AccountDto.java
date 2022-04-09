package sigma.Spring_backend.submall.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AccountDto {
	@ApiModelProperty(value = "은행명", required = true)
	private String bank;
	@ApiModelProperty(value = "계좌번호", required = true)
	private String accountNumber;
	@ApiModelProperty(value = "예금주", required = true)
	private String holderName;
}
