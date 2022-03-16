package sigma.Spring_backend.clientLook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.clientLook.entity.ClientLookPage;

@Repository
public interface ClientLookPageRepository extends JpaRepository<ClientLookPage, Long> {
}