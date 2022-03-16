package sigma.Spring_backend.reservation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.reservation.entity.CancelReason;

@Repository
public interface CancelReasonRepo extends JpaRepository<CancelReason, Long> {
}
