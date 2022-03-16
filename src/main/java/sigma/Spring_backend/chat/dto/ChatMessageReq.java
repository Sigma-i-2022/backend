package sigma.Spring_backend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.chat.entity.ChatMessage;
import sigma.Spring_backend.chat.entity.MessageType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageReq {
	@NotBlank
	private Long chatRoomId;
	@NotBlank
	private MessageType chatType;
	@NotBlank
	private String senderId;
	@NotBlank
	@Size(min = 1, max = 1000)
	private String message;

	public ChatMessage toEntity() {
		return ChatMessage.builder()
				.chatType(chatType)
				.senderId(senderId)
				.message(message)
				.regDate(new DateConfig().getNowDate())
				.chatRoomId(chatRoomId)
				.build();
	}
}
