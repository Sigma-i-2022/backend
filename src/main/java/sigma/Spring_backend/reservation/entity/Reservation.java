package sigma.Spring_backend.reservation.entity;

import lombok.*;
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
	private String confirmedReserveTime; 		// HH:mm - HH:mm

	@Enumerated(EnumType.STRING)
	private CrdiServiceType serviceType;		// 상품 타입

	@Enumerated(EnumType.STRING)
	private CrdiServiceSystem serviceSystem;	// 상담 방법 (줌, 오카)

	@Column(nullable = false, length = 500)
	private String requireText;					// 예약 시 요구사항 작성

	@Setter
	@Column(nullable = false)
	private String activateYnOfClient;	// 클라이언트 예약목록 중 가리기 여부

	@Setter
	@Column(nullable = false)
	private String activateYnOfCrdi;	// 코디 예약목록 중 가리기 여부

	@Setter
	@Column(nullable = false)
	private String confirmResvYn;		// 코디네이터 예약 확정 여부

	@Setter
	@Column
	private String payYn;				// 카드 or 가상계좌 결제 여부

	@Setter
	@Column(nullable = false)
	private String confirmPayYn;		// 구매 확정 여부 (리뷰 작성 용)

	@Setter
	@Column(nullable = false)
	private String cancelYn;			// 취소 여부

	@Setter
	@Column
	private String reviewedYn;			// 리뷰 작성 여부

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
				.reserveTimes(reserveTimes)
				.confirmedReserveTime(confirmedReserveTime)
				.requireText(requireText)
				.payYn(payYn)
				.confirmResvYn(confirmResvYn)
				.confirmPayYn(confirmPayYn)
				.reviewedYn(reviewedYn)
				.cancelYn(cancelYn)
				.build();
	}
}
