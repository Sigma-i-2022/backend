package sigma.Spring_backend.payment.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.payment.entity.Payment;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentReq {
	@ApiModelProperty(value = "예약번호", required = true)
	private Long reservationSeq;
	@ApiModelProperty(value = "지불방법", required = true)
	private PAY_TYPE payType;
	@ApiModelProperty(value = "지불금액", required = true)
	private Long amount;
	@ApiModelProperty(value = "주문 상품 이름", required = true)
	private ORDER_NAME_TYPE orderName;
	@ApiModelProperty(value = "구매자 이메일", required = true)
	private String customerEmail;
	@ApiModelProperty(value = "구매자 이름", required = true)
	private String customerName;

	public Payment toEntity() {
		return Payment.builder()
				.orderId(UUID.randomUUID().toString())
				.reservationSeq(reservationSeq)
				.payType(payType)
				.amount(amount)
				.orderName(orderName)
				.customerEmail(customerEmail)
				.customerName(customerName)
				.cancelYn("N")
				.paySuccessYn("N")
				.createDate(new DateConfig().getNowDate())
				.build();
	}
}
