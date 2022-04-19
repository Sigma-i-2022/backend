package sigma.Spring_backend.submall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubmallResDto {
	private String crdiEmail;
	private String submallId;
	private String companyName;
	private String representativeName;
	private String businessNumber;
	private String type;
	private String bank;
	private String accountNumber;
}
