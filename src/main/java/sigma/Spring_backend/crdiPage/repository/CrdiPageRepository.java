package sigma.Spring_backend.crdiPage.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.crdiPage.entity.CrdiMypage;



@Repository
public interface CrdiPageRepository extends JpaRepository<CrdiMypage, Long> {
    Boolean existsByEmail(String email);

    CrdiMypage findByEmail(String email);

}
