package sigma.Spring_backend.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TossErrorDto {
	String code;
	String message;
}
