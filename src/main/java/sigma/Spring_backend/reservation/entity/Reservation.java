package sigma.Spring_backend.reservation.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.reservation.dto.CrdiServiceSystem;
import sigma.Spring_backend.reservation.dto.CrdiServiceType;
import sigma.Spring_backend.reservation.dto.ReserveRes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reservation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private Long seq;

	@Column(nullable = false)
	private String clientEmail;

	@Column(nullable = false)
	private String crdiEmail;

	@Column(nullable = false)
	private String crdiId;

	@Column(nullable = false)
	private String clientId;

	@Column(nullable = false)
	private String reserveDay; // yyyy년 MM월 dd일

	@Column(nullable = false)
	private String reserveTimes;

	@Setter
	@Column(nullable = false)
	private String confirmedReserveTime; // HH:mm - HH:mm

	@Enumerated(EnumType.STRING)
	private CrdiServiceType serviceType;

	@Enumerated(EnumType.STRING)
	private CrdiServiceSystem serviceSystem;

	@Column(nullable = false, length = 500)
	private String requireText;

	@Setter
	@Column(nullable = false)
	private String activateYnOfClient;

	@Setter
	@Column(nullable = false)
	private String activateYnOfCrdi;

	@Setter
	@Column(nullable = false)
	private String confirmResvYn;

	@Setter
	@Column(nullable = false)
	private String confirmPayYn;

	@Setter
	@Column(nullable = false)
	private String cancelYn;

	@Setter
	@Column
	private String reviewedYn;

	@Setter
	@OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Builder.Default
	private List<MemberReservation> memberReservations = new ArrayList<>();

	public void addMemberReservation(MemberReservation memberReservation) {
		this.memberReservations.add(memberReservation);
		memberReservation.setReservation(this);
	}

	public ReserveRes toDto() {
		return ReserveRes.builder()
				.seq(seq)
				.crdiId(crdiId)
				.clientId(clientId)
				.serviceType(serviceType)
				.serviceSystem(serviceSystem)
				.reserveDay(reserveDay)
				.price(3000)
				.reserveDay(reserveDay)
				.reserveTimes(reserveTimes)
				.confirmedReserveTime(confirmedReserveTime)
				.requireText(requireText)
				.confirmResvYn(confirmResvYn)
				.confirmPayYn(confirmPayYn)
				.reviewedYn(reviewedYn)
				.cancelYn(cancelYn)
				.build();
	}
}
