package sigma.Spring_backend.review.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.review.entity.Review;

@Repository
public interface ReviewRepo extends JpaRepository<Review, Long> {
}
