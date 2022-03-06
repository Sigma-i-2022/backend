package sigma.Spring_backend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.chat.entity.ChatMessage;
import sigma.Spring_backend.chat.entity.MessageType;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReq {
	private MessageType chatType;
	private MultipartFile imageFile;
	private String senderProfileImgUrl;
	private String senderId;
	private String message;
	private LocalDateTime regDate;
	private Long chatRoomId;

	public ChatMessage toEntity(@Nullable String imagePath) {
		if (chatType.equals(MessageType.IMAGE)) {
			return ChatMessage.builder()
					.chatType(chatType)
					.imagePathUrl(imagePath)
					.senderProfileImgUrl(senderProfileImgUrl)
					.senderId(senderId)
					.message(message)
					.regDate(regDate)
					.chatRoomId(chatRoomId)
					.build();
		}
		return ChatMessage.builder()
				.chatType(chatType)
				.senderProfileImgUrl(senderProfileImgUrl)
				.senderId(senderId)
				.message(message)
				.regDate(regDate)
				.chatRoomId(chatRoomId)
				.build();
	}
}
