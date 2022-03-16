package sigma.Spring_backend.chat.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.chat.dto.ChatMessageReq;
import sigma.Spring_backend.chat.dto.ChatMessageRes;
import sigma.Spring_backend.chat.dto.ChatRoomDto;
import sigma.Spring_backend.chat.service.ChatService;

@Slf4j
@Api(tags = "11. 채팅")
@RestController
@RequestMapping("/v1/api/chat")
@RequiredArgsConstructor
public class ChatController {

	private final ChatService chatService;
	private final ResponseService responseService;

	private final int FAIL = -1;

	@PostMapping("/room")
	@ApiOperation(value = "채팅방 생성", notes = "채팅방 생성")
	public SingleResult<ChatRoomDto> createChatRoom(
			@ApiParam @RequestParam String clientEmail,
			@ApiParam @RequestParam String cordiEmail
	) {
		try {
			ChatRoomDto chatRoom = chatService.createChatRoom(clientEmail, cordiEmail);
			return responseService.getSingleResult(chatRoom);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/room")
	@ApiOperation(value = "채팅방 조회", notes = "채팅방을 SEQ로 조회합니다.")
	public SingleResult<ChatRoomDto> getChatRoom(
			@ApiParam @RequestParam Long chatRoomId
	) {
		try {
			return responseService.getSingleResult(
					chatService.findChatRoomById(chatRoomId)
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/members/{chatRoomId}")
	@ApiOperation(value = "채팅방 인원 추가", notes = "채팅방에 참여자를 추가합니다.")
	public CommonResult addMRoomMembers(
			@ApiParam(value = "채팅방 ID (SEQ_PK)") @PathVariable Long chatRoomId,
			@ApiParam(value = "고객 이메일") @RequestParam String memberEmail,
			@ApiParam(value = "코디네이터 이메일") @RequestParam String cordiEmail
	) {
		boolean result = chatService.joinMembersToRoom(chatRoomId, memberEmail, cordiEmail);
		if (result) {
			return responseService.getSuccessResult();
		} else {
			return responseService.getFailResult(
					FAIL,
					ExMessage.CHAT_ERROR_MEMBER_ADD.getMessage()
			);
		}
	}

	@GetMapping("/member/rooms")
	@ApiOperation(value = "회원의 모든 채팅방 조회", notes = "회원이 참여중인 모든 채팅방을 조회합니다.")
	public ListResult<ChatRoomDto> getMemberChatRooms(
			@ApiParam(value = "회원 이메일") @RequestParam String memberEmail
	) {
		try {
			return responseService.getListResult(chatService.findAllChatRoom(memberEmail, "M"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/cordi/rooms")
	@ApiOperation(value = "코디의 모든 채팅방 조회", notes = "코디가 참여중인 모든 채팅방을 조회합니다.")
	public ListResult<ChatRoomDto> getCordiChatRooms(
			@ApiParam(value = "코디 이메일") @RequestParam String cordiEmail
	) {
		try {
			return responseService.getListResult(chatService.findAllChatRoom(cordiEmail, "C"));
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@GetMapping("/all")
	@ApiOperation(value = "채팅방 메세지 조회", notes = "페이징 방식으로 채팅방의 메시지를 최근 순서로 조회합니다.")
	public ListResult<ChatMessageRes> getAllChats(
			@ApiParam(value = "채팅방 SEQ") @RequestParam Long chatRoomSeq,
			@ApiParam(value = "PAGE 번호 (0부터)") @RequestParam(defaultValue = "0") int page,
			@ApiParam(value = "PAGE 크기") @RequestParam(defaultValue = "20") int size
	) {
		try {
			PageRequest pageRequest = PageRequest.of(page, size, Sort.by("regDate").descending());
			return responseService.getListResult(
					chatService.findAllChats(chatRoomSeq, pageRequest).getContent()
			);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}

	@PostMapping("/message")
	@ApiOperation(value = "채팅 전송", notes = "채팅방에 메시지를 전송합니다.")
	public CommonResult sendChatMessage(
			@ApiParam(value = "채팅 메시지") @ModelAttribute ChatMessageReq chatMessageReq,
			@ApiParam(value = "이미지 파일") @RequestParam(required = false) @Nullable MultipartFile imageFile
	) {
		try {
			chatService.sendChat(chatMessageReq, imageFile);
			return responseService.getSuccessResult();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(e.getMessage());
		}
	}
}
