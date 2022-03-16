package sigma.Spring_backend.reservation.entity;

import lombok.*;
import sigma.Spring_backend.reservation.dto.CancelReasonDto;
import sigma.Spring_backend.reservation.dto.TYPE;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CancelReason {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ", nullable = false)
	private Long seq;

	@Column(nullable = false)
	private Long reservationSeq;

	@Column(nullable = false, length = 300)
	private String reason;

	@Column(nullable = false)
	private TYPE byWho;

	public CancelReasonDto toDto() {
		return CancelReasonDto.builder()
				.reservationSeq(reservationSeq)
				.reason(reason)
				.byWho(byWho)
				.build();
	}
}
