package sigma.Spring_backend.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.chat.entity.MemberChatRoomConnection;

@Repository
public interface MemberChatRoomConnectionRepository extends JpaRepository<MemberChatRoomConnection, Long> {
}