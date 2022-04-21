package sigma.Spring_backend.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;
import sigma.Spring_backend.review.entity.Reply;

import java.util.Optional;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {

    Optional<Reply> findByReviewSeq(Long seq);

}
