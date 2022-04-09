package sigma.Spring_backend.submall.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.submall.dto.SubmallResDto;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Submall {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq", nullable = false)
	private Long seq;

	@Column(nullable = false, unique = true)
	private String crdiEmail;

	@Column(nullable = false)
	private String submallId;

	@Column(nullable = false)
	private String companyName;

	@Column(nullable = false)
	private String representativeName;

	@Column
	private String businessNumber;

	@Column
	private String type;

	@Column(nullable = false)
	private String accountNumber;

	@Column(nullable = false)
	private String bank;

	@Setter
	@OneToOne
	private Member crdi;

	public SubmallResDto toDto() {
		return SubmallResDto.builder()
				.submallId(submallId)
				.companyName(companyName)
				.representativeName(representativeName)
				.businessNumber(businessNumber)
				.type(type)
				.accountNumber(accountNumber)
				.bank(bank)
				.build();
	}
}
