package sigma.Spring_backend.submall.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TosspaymentSubmallUpdateReq {
	private String companyName;
	private String representativeName;
	private String businessNumber;
	private TosspaymentSubmallAccountDto account;
}
