package sigma.Spring_backend.clientLook.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.clientLook.dto.ClientLookPageRes;
import sigma.Spring_backend.clientLook.entity.ClientLookPage;

import java.util.List;

@Repository
public interface ClientLookPageRepository extends JpaRepository<ClientLookPage, Long> {
	List<ClientLookPage> findAllByClientEmail(String email, Pageable pageable);
}