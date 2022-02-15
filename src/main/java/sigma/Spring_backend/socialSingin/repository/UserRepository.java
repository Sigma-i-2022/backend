package sigma.Spring_backend.socialSingin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.Spring_backend.socialSingin.domain.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
}
