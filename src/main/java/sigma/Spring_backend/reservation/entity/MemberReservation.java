package sigma.Spring_backend.reservation.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberReservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ", nullable = false)
	private Long seq;

	@Setter
	@ManyToOne
	@JoinColumn(name = "MEMBER_SEQ")
	private Member member;

	@Setter
	@ManyToOne
	@JoinColumn(name = "RESERVATION_SEQ")
	private Reservation reservation;
}
