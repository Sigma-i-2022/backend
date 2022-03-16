package sigma.Spring_backend.memberMypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;

import java.util.Optional;

@Repository
public interface CommonMypageRepository extends JpaRepository<CommonMypage, Long> {

	Optional<CommonMypage> findByEmail(String email);

	Boolean existsByEmail(String email);

	void deleteByEmail(String email);
}
