package sigma.Spring_backend.memberSignup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorizeCodeRepo extends JpaRepository<AuthorizeEntity, Long> {

	Optional<AuthorizeEntity> findByEmail(String email);
}
