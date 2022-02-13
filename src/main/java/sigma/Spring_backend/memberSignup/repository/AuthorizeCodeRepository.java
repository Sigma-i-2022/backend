package sigma.Spring_backend.memberSignup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;

import java.util.Optional;

@Repository
public interface AuthorizeCodeRepository extends JpaRepository<AuthorizeMember, Long> {

	Optional<AuthorizeMember> findByEmail(String email);
}
