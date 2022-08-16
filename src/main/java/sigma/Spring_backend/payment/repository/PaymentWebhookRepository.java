package sigma.Spring_backend.payment.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.payment.entity.PaymentWebhook;

@Repository
public interface PaymentWebhookRepository extends JpaRepository<PaymentWebhook, Long> {
}
