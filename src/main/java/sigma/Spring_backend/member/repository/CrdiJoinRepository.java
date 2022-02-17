package sigma.Spring_backend.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.member.entity.JoinCrdi;

import java.util.Optional;

@Repository
public interface CrdiJoinRepository extends JpaRepository<JoinCrdi, Long> {

        Optional<JoinCrdi> findByEmail(String email);
}
