package sigma.Spring_backend.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;
import sigma.Spring_backend.payment.entity.Payment;
import sigma.Spring_backend.payment.repository.PaymentRepository;
import sigma.Spring_backend.reservation.dto.ReservePartTimeReq;
import sigma.Spring_backend.reservation.dto.ReserveReq;
import sigma.Spring_backend.reservation.dto.ReserveRes;
import sigma.Spring_backend.reservation.entity.MemberReservation;
import sigma.Spring_backend.reservation.entity.Reservation;
import sigma.Spring_backend.reservation.repository.MemberReservationRepo;
import sigma.Spring_backend.reservation.repository.ReservationRepo;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

	private final MemberRepository memberRepository;
	private final ReservationRepo reservationRepo;
	private final MemberReservationRepo memberReservationRepo;
	private final PaymentRepository paymentRepository;

	@Transactional(readOnly = true)
	public ReserveRes getReservation(Long seq) {
		return reservationRepo.findById(seq)
				.orElseThrow(() -> new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND))
				.toDto();
	}

	@Transactional(readOnly = true)
	public List<ReserveRes> getAllReservations() {
		return reservationRepo.findAll()
				.stream().map(Reservation::toDto)
				.collect(Collectors.toList());
	}

	@Transactional
	public boolean reserveCrdi(ReserveReq reserveReq) {
		boolean verify = verifyReserveReq(reserveReq);
		if (!verify) throw new BussinessException(ExMessage.RESERVATION_ERROR_FORMAT);

		try {
			Reservation reservation = reserveReq.toEntity();
			Member client = memberRepository.findByEmailFJ(reserveReq.getClientEmail())
					.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
			Member crdi = memberRepository.findByEmailFJ(reserveReq.getCrdiEmail())
					.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

			MemberReservation clientReservation = MemberReservation.builder()
					.member(client)
					.reservation(reservation)
					.build();
			MemberReservation crdiReservation = MemberReservation.builder()
					.member(crdi)
					.reservation(reservation)
					.build();

			memberReservationRepo.save(clientReservation);
			memberReservationRepo.save(crdiReservation);

			client.registReservation(clientReservation);
			crdi.registReservation(crdiReservation);

			reservation.addMemberReservation(clientReservation);
			reservation.addMemberReservation(crdiReservation);

			reservationRepo.save(reservation);
		} catch (Exception e) {
			throw new BussinessException(ExMessage.DB_ERROR_SAVE);
		}
		return true;
	}

	private boolean verifyReserveReq(ReserveReq reserveReq) {
		String clientEmail = reserveReq.getClientEmail();
		String crdiEmail = reserveReq.getCrdiEmail();
		Member client = memberRepository.findByEmailFJ(clientEmail)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		Member crdi = memberRepository.findByEmailFJ(crdiEmail)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

		if (client.getCrdiYn().equals("Y")) {
			log.error("WRONG CRDI INFO");
			return false;
		}

		if (crdi.getCrdiYn().equals("N")) {
			log.error("WRONG CRDI INFO");
			return false;
		}

		if (reserveReq.getServiceType() == null || reserveReq.getServiceSystem() == null) {
			log.error("NO SERVICE DATA");
			return false;
		}

		// 현재보다 빠르면 안댐
		if (reserveReq.getReserveDay() == null ||
				reserveReq.getReserveDay().compareTo(new DateConfig().getNowDate().substring(0, 10)) < 0) {
			log.error("WRONG DAY DATA");
			return false;
		}

		return true;
	}

	@Transactional(readOnly = true)
	public List<ReserveRes> getAllReservationOfMember(String email, PageRequest pageRequest) {
		Member member = memberRepository.findByEmailFJ(email)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

		if (member.getCrdiYn().equals("Y" )) {
			return reservationRepo.findAllByCrdiEmail(email, pageRequest)
					.stream()
					.filter(R -> R.getActivateYnOfCrdi().equals("Y"))
					.map(Reservation::toDto)
					.collect(Collectors.toList());
		} else {
			return reservationRepo.findAllByClientEmail(email, pageRequest)
					.stream()
					.filter(R -> R.getActivateYnOfClient().equals("Y"))
					.map(Reservation::toDto)
					.collect(Collectors.toList());
		}

	}

	@Transactional
	public boolean hideReservation(String memberEmail, Long reservationSeq) {
		Reservation reservation = reservationRepo.findById(reservationSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND));

		memberRepository
				.findByEmailFJ(memberEmail)
				.ifPresentOrElse(
						M -> {
							if (M.getCrdiYn().equals("Y"))
								reservation.setActivateYnOfCrdi("N");
							else if (M.getCrdiYn().equals("N"))
								reservation.setActivateYnOfClient("N");
						}, () -> {
							throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);
						}
				);
		return true;
	}

	@Transactional
	public void confirmReservation(String crdiEmail, Long reservationSeq, ReservePartTimeReq resvTime) {

		Payment payment = paymentRepository.findByReservationSeq(reservationSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.PAYMENT_ERROR_ORDER_NOTFOUND));

		if (payment.getCancelYn().equals("Y")) {
			throw new BussinessException(ExMessage.RESERVATION_ERROR_ALREADY_CANCEL);
		}

		if (payment.getPaySuccessYn().equals("N")) {
			throw new BussinessException(ExMessage.PAYMENT_ERROR_NOT_PAY);
		}

		reservationRepo.findById(reservationSeq)
				.filter(R -> R.getCrdiEmail().equals(crdiEmail))
				.ifPresentOrElse(
						R -> {
							if (R.getCancelYn().equals("Y")) {
								throw new BussinessException(ExMessage.RESERVATION_ERROR_ALREADY_CANCEL);
							}
							if (R.getPayYn().equals("N")) {
								throw new BussinessException(ExMessage.RESERVATION_ERROR_NOT_PAY);
							}
							R.setConfirmResvYn("Y");
							R.setConfirmedReserveTime(resvTime.toString());
						}, () -> {
							throw new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND);
						}
				);
	}

	@Transactional
	public boolean confirmPay(String clientEmail, Long reservationSeq) {
		reservationRepo.findById(reservationSeq)
				.filter(R -> R.getClientEmail().equals(clientEmail))
				.filter(R -> R.getConfirmResvYn().equals("Y")) // 코티네이터가 예약 확정
				.ifPresentOrElse(
						R -> R.setConfirmPayYn("Y")
						, () -> {
							throw new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND);
						}
				);
		return true;
	}
}
