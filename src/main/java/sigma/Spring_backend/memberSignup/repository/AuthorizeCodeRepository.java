package sigma.Spring_backend.memberSignup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.memberSignup.entity.AuthorizeEntity;

import java.util.Optional;

@Repository
public interface AuthorizeCodeRepository extends JpaRepository<AuthorizeEntity, Long> {

	Optional<AuthorizeEntity> findByEmail(String email);
}
