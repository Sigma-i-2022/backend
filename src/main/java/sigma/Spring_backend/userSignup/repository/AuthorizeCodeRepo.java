package sigma.Spring_backend.userSignup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.userSignup.entity.AuthorizeEntity;

import java.util.Optional;

@Repository
public interface AuthorizeCodeRepo extends JpaRepository<AuthorizeEntity, Long> {

	Optional<AuthorizeEntity> findByEmail(String email);
}
