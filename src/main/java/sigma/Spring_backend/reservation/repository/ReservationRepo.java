package sigma.Spring_backend.reservation.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.reservation.entity.Reservation;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

	@Query("select r from Reservation r where r.clientEmail = ?1")
	List<Reservation> findAllByClientEmail(String clientEmail, Pageable pageable);

	@Query("select r from Reservation r where r.crdiEmail = ?1")
	List<Reservation> findAllByCrdiEmail(String crdiEmail, Pageable pageable);
}
