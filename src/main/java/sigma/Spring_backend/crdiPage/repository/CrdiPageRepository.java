package sigma.Spring_backend.crdiPage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.crdiPage.entity.CrdiMyPage;

@Repository
public interface CrdiPageRepository extends JpaRepository<CrdiMyPage, Long> {
    Boolean existsByEmail(String email);

}
