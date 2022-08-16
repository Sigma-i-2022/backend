package sigma.Spring_backend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageRes {
	private String chatType;
	private String imagePathUrl;
	private String senderProfileImgUrl;
	private String message;
	private String senderId;
	private String regDate;
	private Long chatRoomId;
}
