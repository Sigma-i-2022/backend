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

	@Column(nullable = false)
	private String crdiEmail;

	@Column(nullable = false)
	private String submallId;

	@Setter
	@Column(nullable = false)
	private String companyName;

	@Setter
	@Column(nullable = false)
	private String representativeName;

	@Setter
	@Column
	private String businessNumber;

	@Column
	private String type;

	@Setter
	@Column(nullable = false)
	private String accountNumber;

	@Setter
	@Column(nullable = false)
	private String bank;

	@Setter
	@Column(nullable = false)
	private boolean activate;

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CRDI_SEQ")
	private Member crdi;

	public SubmallResDto toDto() {
		return SubmallResDto.builder()
				.crdiEmail(crdiEmail)
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
