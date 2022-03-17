package sigma.Spring_backend.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.review.entity.Reply;

@Repository
public interface ReplyRepository extends JpaRepository<Reply, Long> {
}
