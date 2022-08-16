package sigma.Spring_backend.crdiPage.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;


import java.util.List;

@Repository
public interface CrdiWorkRepository extends JpaRepository<CrdiWork, Long> {
    List<CrdiWork> findAllByCrdiEmail(String crdiEmail, Pageable pageable);
}
