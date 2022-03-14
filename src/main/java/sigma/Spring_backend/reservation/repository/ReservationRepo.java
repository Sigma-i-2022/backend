package sigma.Spring_backend.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.reservation.entity.Reservation;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

	@Query("select r from Reservation r where r.clientId = ?1")
	List<Reservation> findAllByClientId(String clientId);

	@Query("select r from Reservation r where r.crdiId = ?1")
	List<Reservation> findAllByCrdiId(String crdiId);
}
