package sigma.Spring_backend.crdiBlock.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.crdiBlock.entity.CrdiBlock;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrdiBlockRepository extends JpaRepository<CrdiBlock, Long> {

    @Query("select b.crdiEmail from CrdiBlock b where b.email = ?1")
    List<String> findByEmail(String email);
}
