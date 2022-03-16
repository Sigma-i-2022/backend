package sigma.Spring_backend.memberReport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.memberReport.entity.MemberReport;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberReportRepo extends JpaRepository<MemberReport, Long> {

	List<MemberReport> findAllByMemberSeq(Long memberSeq);
}
