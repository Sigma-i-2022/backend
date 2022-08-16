package sigma.Spring_backend.payment.dto;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDto {
	private Long seq;
	private Long reservationSeq;
	private String payType;
	private Long amount;
	private String cardCompany;
	private String cardNumber;			// "949129******7058"
	private String cardReceiptUrl;
	private String orderId;
	private String orderName;
	private String customerEmail;
	private String customerName;
	private String paymentKey;
	private String paySuccessYn;
	private String payFailReason;
	private String cancelYn;
	private String createDate;
}
