package sigma.Spring_backend.submall.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.submall.entity.Submall;

import java.util.Optional;

@Repository
public interface SubmallRepository extends JpaRepository<Submall, Long> {
	Optional<Submall> findByCrdiEmail(String email);
}
