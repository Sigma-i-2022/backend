package sigma.Spring_backend.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import sigma.Spring_backend.reservation.entity.MemberReservation;

import java.util.List;

public interface MemberReservationRepo extends JpaRepository<MemberReservation, Long> {

	@Query("select m from MemberReservation m where m.member.seq = ?1")
	List<MemberReservationRepo> findAllByMemberSeq(Long memberSeq);
}
