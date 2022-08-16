package sigma.Spring_backend.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TossVirtualDto {
	private String secret;
	private String status;
	private String orderId;
}
