package sigma.Spring_backend.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RefundReceiveAccountDto {
	@ApiModelProperty(value = "환불 입금 기관")
	private String bank;
	@ApiModelProperty(value = "환불 입금 계좌번호")
	private String accountNumber;
	@ApiModelProperty(value = "환불 입금 예금주 성함")
	private String holderName;
}
