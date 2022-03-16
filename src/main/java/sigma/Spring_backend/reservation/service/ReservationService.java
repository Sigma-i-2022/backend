package sigma.Spring_backend.reservation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;
import sigma.Spring_backend.reservation.dto.ReservePartTimeReq;
import sigma.Spring_backend.reservation.dto.ReserveReq;
import sigma.Spring_backend.reservation.dto.ReserveRes;
import sigma.Spring_backend.reservation.dto.TYPE;
import sigma.Spring_backend.reservation.entity.CancelReason;
import sigma.Spring_backend.reservation.entity.MemberReservation;
import sigma.Spring_backend.reservation.entity.Reservation;
import sigma.Spring_backend.reservation.repository.CancelReasonRepo;
import sigma.Spring_backend.reservation.repository.MemberReservationRepo;
import sigma.Spring_backend.reservation.repository.ReservationRepo;

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
	private final CancelReasonRepo cancelReasonRepo;

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
			Member client = memberRepository.findByIdFJ(reserveReq.getClientId())
					.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
			Member crdi = memberRepository.findByIdFJ(reserveReq.getCrdiId())
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
		String clientId = reserveReq.getClientId();
		String crdiId = reserveReq.getCrdiId();
		Member client = memberRepository.findByIdFJ(clientId)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		Member crdi = memberRepository.findByIdFJ(crdiId)
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

		// 예약 시간의 끝이 시작보다 빠르면 안댐
		for (ReservePartTimeReq reserveTime : reserveReq.getReserveTimes().getReservePartTimeReqs()) {
			if (reserveTime.getStartTime().compareTo(reserveTime.getEndTime()) >= 0) {
				log.error("WRONG TIME DATA");
				return false;
			}
		}

		return true;
	}

	@Transactional(readOnly = true)
	public List<ReserveRes> getAllReservationOfMember(String memberId) {
		Member member = memberRepository.findByIdFJ(memberId)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		if (member.getCrdiYn().equals("Y" )) {
			return reservationRepo.findAllByCrdiId(memberId)
					.stream().map(Reservation::toDto)
					.collect(Collectors.toList());
		} else {
			return reservationRepo.findAllByClientId(memberId)
					.stream().map(Reservation::toDto)
					.collect(Collectors.toList());
		}

	}

	@Transactional
	public boolean hideReservation(String memberId, Long reservationSeq) {
		Reservation reservation = reservationRepo.findById(reservationSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND));

		memberRepository
				.findByIdFJ(memberId)
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
	public boolean confirmReservation(String crdiId, Long reservationSeq, ReservePartTimeReq resvTime) {
		reservationRepo.findById(reservationSeq)
				.filter(R -> R.getCrdiId().equals(crdiId))
				.ifPresentOrElse(
						R -> {
							R.setConfirmResvYn("Y");
							R.setConfirmedReserveTime(resvTime.toString());
						}, () -> {
							throw new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND);
						}
				);
		return true;
	}

	@Transactional
	public boolean confirmPay(String clientId, Long reservationSeq) {
		reservationRepo.findById(reservationSeq)
				.filter(R -> R.getClientId().equals(clientId))
				.filter(R -> R.getConfirmResvYn().equals("Y")) // 코티네이터가 예약 확정
				.ifPresentOrElse(
						R -> R.setConfirmPayYn("Y")
						, () -> {
							throw new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND);
						}
				);
		return true;
	}

	@Transactional
	public boolean cancelResvByCrdi(String crdiId, Long reservationSeq, String reason) {
		reservationRepo.findById(reservationSeq)
				.filter(R -> R.getCrdiId().equals(crdiId))
				.filter(R -> R.getConfirmResvYn().equals("N"))
				.ifPresentOrElse(
						R -> {
							R.setCancelYn("Y");
							cancelReasonRepo.save(CancelReason.builder()
									.reservationSeq(reservationSeq)
									.reason(reason)
									.byWho(TYPE.CRDI)
									.build());
						}, () -> {
							throw new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND);
						}
				);
		return true;
	}

	@Transactional
	public boolean cancelResvByClient(String clientId, Long reservationSeq, String reason) {
		String after6HourOfNow = LocalDateTime.now()
				.plusHours(6)
				.format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm"));

		Reservation reservation = reservationRepo.findById(reservationSeq)
				.filter(R -> R.getClientId().equals(clientId))
				.orElseThrow(() -> new BussinessException(ExMessage.RESERVATION_ERROR_NOT_FOUND_CLIENT));

		String reservationTime = (reservation.getReserveDay() + " " + reservation.getConfirmedReserveTime())
				.substring(0, 19);

		CancelReason cancelReason = CancelReason.builder()
				.reservationSeq(reservationSeq)
				.reason(reason)
				.byWho(TYPE.CLIENT)
				.build();

		// 6시간 이내에 예약시간 존재하면 취소 불가 or 예약을 안했으면 가능
		if (reservation.getConfirmPayYn().equals("Y")) {
			return false;
		} else if (reservation.getConfirmResvYn().equals("Y") && reservationTime.compareTo(after6HourOfNow) > 0) {
			reservation.setCancelYn("Y");
			cancelReasonRepo.save(cancelReason);
		} else if (reservation.getConfirmResvYn().equals("N")) {
			reservation.setCancelYn("Y");
			cancelReasonRepo.save(cancelReason);
		} else return false;

		return true;
	}
}
