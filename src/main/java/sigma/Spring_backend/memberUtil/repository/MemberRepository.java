package sigma.Spring_backend.memberUtil.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.memberUtil.entity.Member;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByEmail(String email);

    Boolean existsByEmail(String email);

    @Query("select m from Member m join fetch m.mypage join fetch m.authorizeUser where m.seq=?1")
    Optional<Member> findBySeqFJ(Long seq);

    @Query("select m from Member m join fetch m.mypage join fetch m.authorizeUser where m.email=?1")
    Optional<Member> findByEmailFJ(String email);

    @Query("select m from Member m join fetch m.mypage join fetch m.authorizeUser where m.refreshToken=?1")
    Optional<Member> findByRefreshTokenFJ(String token);

    List<Member> findByCrdiYnAndEmailNotIn(String crdiYn,List<String> crdiEmail);

    List<Member> findByCrdiYn(String crdiYn);
}
