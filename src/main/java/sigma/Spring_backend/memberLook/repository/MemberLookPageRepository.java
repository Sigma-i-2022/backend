package sigma.Spring_backend.memberLook.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.memberLook.entity.MemberLookPage;

@Repository
public interface MemberLookPageRepository extends JpaRepository<MemberLookPage, Long> {
}