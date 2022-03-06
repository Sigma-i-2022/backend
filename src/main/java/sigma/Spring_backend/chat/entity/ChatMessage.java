package sigma.Spring_backend.chat.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import sigma.Spring_backend.chat.dto.ChatMessageRes;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {

	@Id
	@Column(name = "SEQ", nullable = false, unique = true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@Column
	@Enumerated(EnumType.STRING)
	private MessageType chatType;

	@Column
	private String imagePathUrl;

	@Column
	private String senderProfileImgUrl;

	@Column
	private String senderId;

	@Column(length = 1000)
	private String message;

	@Column
	private Long chatRoomId;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime regDate;

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHAT_ROOM_SEQ")
	private ChatRoom chatRoom;

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}

	public ChatMessageRes toDto() {
		return ChatMessageRes.builder()
				.chatType(chatType.name())
				.imagePathUrl(imagePathUrl)
				.senderProfileImgUrl(senderProfileImgUrl)
				.senderId(senderId)
				.regDate(regDate)
				.chatRoomId(chatRoomId)
				.message(message)
				.build();
	}
}
