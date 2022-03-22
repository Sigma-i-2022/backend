package sigma.Spring_backend.payment.entity;

import lombok.*;
import sigma.Spring_backend.payment.dto.TossWebhookDataDto;
import sigma.Spring_backend.payment.dto.TossWebhookDto;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentWebhook {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq", nullable = false)
	private Long seq;

	@Column
	@Setter
	private Long paymentSeq;

	@Column
	private String eventType;

	@Column
	private String paymentKey;

	@Column
	private String status;

	@Column
	private String orderId;

	public TossWebhookDto toDto() {
		return TossWebhookDto.builder()
				.paymentSeq(paymentSeq)
				.eventType(eventType)
				.data(TossWebhookDataDto
						.builder()
						.paymentKey(paymentKey)
						.status(status)
						.orderId(orderId)
						.build()
				)
				.build();
	}
}
