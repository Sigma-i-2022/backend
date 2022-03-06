package sigma.Spring_backend.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import sigma.Spring_backend.chat.dto.ChatMessageRes;
import sigma.Spring_backend.chat.dto.ChatRoomDto;
import sigma.Spring_backend.chat.entity.ChatRoom;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

	@Query("select c from ChatRoom c where c.clientId = ?1")
	Optional<List<ChatRoom>> findAllByClientId(String clientId);

	@Query("select c from ChatRoom c where c.cordiId = ?1")
	Optional<List<ChatRoom>> findAllByCordiId(String cordId);
}