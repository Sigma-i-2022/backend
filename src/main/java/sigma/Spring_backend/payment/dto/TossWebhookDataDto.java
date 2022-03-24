package sigma.Spring_backend.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TossWebhookDataDto {
	private String paymentKey;
	private String status;
	private String orderId;
}
