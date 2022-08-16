package sigma.Spring_backend.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelPaymentRes {
	private Long seq;
	private String orderId;
	private String paymentKey;
	private String orderName;
	private String requestedAt;
	private String approvedAt;
	private String cardCompany;
	private String cardNumber;
	private String receiptUrl;
	private String cancelReason;
	private String cancelDate;
	private Long cancelAmount;
}
