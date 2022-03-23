package sigma.Spring_backend.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class CancelPaymentReq {
	@ApiModelProperty(value = "토스 측 주문 고유 번호")
	private String paymentKey;
	@ApiModelProperty(value = "결제 취소 사유")
	private String cancelReason;
	@ApiModelProperty(value = "환불 입금 기관")
	private String bank;
	@ApiModelProperty(value = "환불 입금 계좌번호")
	private String accountNumber;
	@ApiModelProperty(value = "환불 입금 예금주 성함")
	private String holderName;

	public RefundReceiveAccountDto getRefundAccountDto() {
		return RefundReceiveAccountDto.builder()
				.bank(bank)
				.accountNumber(accountNumber)
				.holderName(holderName)
				.build();
	}
}
