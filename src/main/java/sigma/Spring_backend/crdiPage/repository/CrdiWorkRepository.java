package sigma.Spring_backend.crdiPage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;

@Repository
public interface CrdiWorkRepository extends JpaRepository<CrdiWork, Long> {
}
