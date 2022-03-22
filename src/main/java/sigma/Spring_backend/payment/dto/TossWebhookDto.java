package sigma.Spring_backend.payment.dto;

import lombok.Data;

@Data
public class TossWebhookDto {
	String eventType;
	TossWebhookDataDto data;
}
