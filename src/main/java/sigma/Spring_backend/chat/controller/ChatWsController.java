package sigma.Spring_backend.chat.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;
import sigma.Spring_backend.chat.dto.ChatMessageReq;
import sigma.Spring_backend.chat.entity.ChatMessage;
import sigma.Spring_backend.chat.service.ChatService;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatWsController {

	private final ChatService chatService;

	/**
	 * Publish [/pub/chat/message/{roomSeq}]
	 */
	@MessageMapping("/chat/message/{roomSeq}")
	@SendTo("/sub/chat/message/{roomSeq}")
	@ApiOperation(value = "채팅 메세지 전송", notes = "채팅방 인원들에게 메시지를 전파합니다.")
	public ChatMessage message(
			@DestinationVariable Long roomSeq,
			ChatMessageReq message,
			@Nullable String uuid
	) {
		return chatService.sendChat(message, uuid);
	}

	@MessageMapping("/chat/notify/{roomSeq}")
	@SendTo("/sub/chat/notify/{roomSeq}")
	@ApiOperation(value = "알림 메시지 전송", notes = "채팅방 인원들에게 알림을 전파합니다.")
	public ChatMessage notify(
			@DestinationVariable Long roomSeq,
			ChatMessage notify
	) {
		return notify;
	}
}
