package sigma.Spring_backend.repository.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.dto.member.MemberResponseDto;
import sigma.Spring_backend.entity.member.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findById(Long id);

    Optional<Member> findByEmail(String email);

    Optional<Member> findByNameAndAddress(String name, String address);

    Optional<List<Member>> findAllByName(String name);
}
