package sigma.Spring_backend.payment.dto;

import lombok.Data;

@Data
public class TossWebhookDataDto {
	String paymentKey;
	String status;
	String orderId;
}
