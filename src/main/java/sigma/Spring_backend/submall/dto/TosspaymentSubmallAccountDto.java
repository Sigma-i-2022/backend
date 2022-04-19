package sigma.Spring_backend.submall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TosspaymentSubmallAccountDto {
	private String bank;
	private String accountNumber;
	private String holderName;
}
