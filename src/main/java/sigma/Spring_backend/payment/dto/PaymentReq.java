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
	@ApiModelProperty("예약번호")
	private Long reservationSeq;
	@ApiModelProperty("지불방법")
	private PAY_TYPE payType;
	@ApiModelProperty("지불금액")
	private Long amount;
	@ApiModelProperty("주문 상품 이름")
	private ORDER_NAME_TYPE orderName;
	@ApiModelProperty("구매자 이메일")
	private String customerEmail;
	@ApiModelProperty("구매자 이름")
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
