package sigma.Spring_backend.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.Spring_backend.payment.entity.PaymentWebhook;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TossWebhookDto {
	private Long paymentSeq;
	private String eventType;
	private TossWebhookDataDto data;

	public PaymentWebhook toEntity() {
		return PaymentWebhook.builder()
				.eventType(eventType)
				.paymentKey(data.getPaymentKey())
				.status(data.getStatus())
				.orderId(data.getOrderId())
				.build();
	}
}
