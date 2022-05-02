package sigma.Spring_backend.chat.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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
	@ApiModelProperty(required = true)
	private Long chatRoomId;
	@ApiModelProperty(required = true)
	private MessageType chatType;
	@ApiModelProperty(required = true)
	private String senderEmail;
	@ApiModelProperty(required = true)
	private String senderId;
	@ApiModelProperty(required = true)
	@Size(min = 1, max = 1000)
	private String message;

	public ChatMessage toEntity() {
		return ChatMessage.builder()
				.chatType(chatType)
				.senderEmail(senderEmail)
				.senderId(senderEmail)
				.message(message)
				.regDate(new DateConfig().getNowDate())
				.chatRoomId(chatRoomId)
				.build();
	}
}
