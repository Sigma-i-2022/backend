package sigma.Spring_backend.account.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sigma.Spring_backend.account.entity.AccountInfo;

import java.util.Optional;

public interface AccountInfoRepository extends JpaRepository<AccountInfo, Long> {
	Optional<AccountInfo> findByCrdiSeq(Long crdiSeq);
}
