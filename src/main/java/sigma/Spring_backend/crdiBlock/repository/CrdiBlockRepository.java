package sigma.Spring_backend.crdiBlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.crdiBlock.entity.CrdiBlock;

@Repository
public interface CrdiBlockRepository extends JpaRepository<CrdiBlock, Long> {
}
