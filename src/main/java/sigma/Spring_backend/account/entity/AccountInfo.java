package sigma.Spring_backend.account.entity;

import lombok.*;
import sigma.Spring_backend.account.dto.OpenApiAccountInfoRes;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountInfo {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq", nullable = false)
	private Long seq;

	@Column(nullable = false, unique = true)
	private Long crdiSeq;

	@Setter
	@Column(nullable = false)
	private String bankName;

	@Setter
	@Column(nullable = false)
	private String bankCode;

	@Setter
	@Column(nullable = false)
	private String birthDay;		// yyMMdd

	@Setter
	@Column(nullable = false)
	private String accountRealName;

	@Setter
	@Column(nullable = false)
	private String accountNum;

	public OpenApiAccountInfoRes toDto() {
		return OpenApiAccountInfoRes.builder()
				.crdiSeq(crdiSeq)
				.bankName(bankName)
				.bankCode(bankCode)
				.birthDay(birthDay)
				.accountRealName(accountRealName)
				.accountNum(accountNum)
				.build();
	}
}
