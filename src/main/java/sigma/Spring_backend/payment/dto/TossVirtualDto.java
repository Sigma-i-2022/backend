package sigma.Spring_backend.payment.dto;

import lombok.Data;

@Data
public class TossVirtualDto {
	private String secret;
	private String status;
	private String orderId;
}
