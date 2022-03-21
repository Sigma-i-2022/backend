package sigma.Spring_backend.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.payment.entity.CancelPayment;

import java.util.Optional;

@Repository
public interface CancelPaymentRepository extends JpaRepository<CancelPayment, Long> {

	Optional<CancelPayment> findByPaymentKey(String orderId);
}
