package sigma.Spring_backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.Spring_backend.account.entity.OpenApiAccessToken;

import java.util.Optional;

public interface AccessTokenRepository extends JpaRepository<OpenApiAccessToken, Long> {

	Optional<OpenApiAccessToken> findFirstByExpireDateAfter(String expireDate);
}
