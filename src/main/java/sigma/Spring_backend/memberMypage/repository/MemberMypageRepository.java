package sigma.Spring_backend.memberMypage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;

import java.util.Optional;

public interface MemberMypageRepository extends JpaRepository<MemberMypage, Long> {

	Optional<MemberMypage> findByEmail(String email);

	Boolean existsByEmail(String email);

	Void deleteByEmail(String email);
}
