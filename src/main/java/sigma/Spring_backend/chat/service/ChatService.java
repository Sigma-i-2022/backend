package sigma.Spring_backend.chat.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.chat.dto.*;
import sigma.Spring_backend.chat.entity.ChatMessage;
import sigma.Spring_backend.chat.entity.ChatRoom;
import sigma.Spring_backend.chat.entity.MemberChatRoomConnection;
import sigma.Spring_backend.chat.entity.MessageType;
import sigma.Spring_backend.chat.repository.ChatMessageRepository;
import sigma.Spring_backend.chat.repository.ChatRoomRepository;
import sigma.Spring_backend.chat.repository.MemberChatRoomConnectionRepository;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

	private final MemberChatRoomConnectionRepository memberChatRoomConnectionRepository;
	private final ChatMessageRepository chatMessageRepository;
	private final ChatRoomRepository chatRoomRepository;
	private final MemberRepository memberRepository;
	private final AwsService awsService;
	/*
	회원채팅방이 생성되는 순간 -> 회원채팅방에 회원이 등록되어야 하고 & 채팅방이 등록되어야 한다.
	회원채팅방 : 회원을 가짐, 채팅방을 가짐
	채팅방 : 생성날, 이름, 메시지 컬렉션을 가짐
	메시지 : 생성날, 타입, 내용,
	 */

	/*
	대화하기 누르면 -> 방생성 but INIT 은 되지 않음
	 */
	@Transactional
	public ChatRoomDto createChatRoom(String clientEmail, String crdiEmail) {
		Member client = memberRepository.findByEmailFJ(clientEmail)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		Member cordi = memberRepository.findByEmailFJ(crdiEmail)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

		ChatRoom chatRoom = ChatRoom.builder()
				.clientId(client.getUserId())
				.clientProfileImageUrl(client.getMypage().getProfileImgUrl())
				.cordiId(cordi.getUserId())
				.cordiProfileImageUrl(cordi.getMypage().getProfileImgUrl())
				.MemberChatRoomConnections(new ArrayList<>())
				.messages(new ArrayList<>())
				.initYn("N")
				.regDate(LocalDateTime.now())
				.build();

		try {
			return chatRoomRepository.save(chatRoom).toDto();
		} catch (Exception e) {
			e.printStackTrace();
			throw new BussinessException(ExMessage.CHAT_ERROR_CREATE);
		}
	}

	@Transactional
	public boolean joinMembersToRoom(Long roomSeq, String mem1, String mem2) {
		ChatRoom chatRoom = chatRoomRepository.findById(roomSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.CHAT_ERROR_CREATE));

		List<String> members = new ArrayList<>();
		members.add(mem1);
		members.add(mem2);
		for (String memberEmail : members) {
			Member member = memberRepository.findByEmailFJ(memberEmail)
					.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

			MemberChatRoomConnection memberChatRoomConnection = MemberChatRoomConnDto.builder()
					.chatRoom(chatRoom)
					.member(member)
					.build().toEntity();
			memberChatRoomConnectionRepository.save(memberChatRoomConnection);
			/*
			관계 설정
			 */
			member.enterChatRoom(memberChatRoomConnection);
			chatRoom.addMemberChatRoom(memberChatRoomConnection);
		}
		try {
			chatRoomRepository.save(chatRoom);
		} catch (Exception e) {
			throw new BussinessException(ExMessage.CHAT_ERROR_CREATE);
		}
		return true;
	}

	@Transactional(readOnly = true)
	public ChatRoomDto findChatRoomById(Long id) {
		ChatRoom chatRoom = chatRoomRepository.findById(id)
				.orElseThrow(() -> new BussinessException(ExMessage.CHAT_ERROR_NOT_FOUND));
		return chatRoom.toDto();
	}

	/*
	채팅입력 : MessageType 별로 나눠서 진행
	 */
	@Transactional
	public ChatMessage sendChat(ChatMessageReq message) {
		Long chatRoomId = message.getChatRoomId();
		String memberId = message.getSenderId();

		ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
				.orElseThrow(() -> new BussinessException(ExMessage.CHAT_ERROR_NOT_FOUND));
		Member member = memberRepository.findByIdFJ(memberId)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

		message.setRegDate(LocalDateTime.now());
		message.setSenderProfileImgUrl(member.getMypage().getProfileImgUrl());

		if (message.getChatType().equals(MessageType.ENTER)) {
			ChatMessage chatMessage = message.toEntity(null);
			chatMessage.setSenderId("[알림]");
			chatMessage.setMessage(member.getUserId() +"님이 들어왔습니다.");
			chatRoom.setInitYn("Y");
			return chatMessage;
		} else if (message.getChatType().equals(MessageType.OUT)) {
			ChatMessage chatMessage = message.toEntity(null);
			chatMessage.setSenderId("[알림]");
			chatMessage.setMessage(member.getUserId() +"님이 나갔습니다.");
			chatRoom.setInitYn("N");
			return chatMessage;
		} else if (message.getChatType().equals(MessageType.IMAGE)) {
			String awsImgUrl = awsService.imageUploadToS3("/chatRoom", message.getImageFile());
			chatRoom.addImage(
					ImageUrl.builder()
					.url(awsImgUrl)
					.build()
			);
			ChatMessage chatMessage = message.toEntity(awsImgUrl);
			chatMessage.setSenderId(member.getUserId());
			chatRoom.addChatMessage(chatMessage);
			return chatMessage;
		} else if (message.getChatType().equals(MessageType.CHAT)) {
			ChatMessage chatMessage = message.toEntity(null);
			chatMessage.setSenderId(member.getUserId());
			chatRoom.addChatMessage(chatMessage);
			return chatMessage;
		}
		throw new BussinessException(ExMessage.CHAT_ERROR_SEND_CHAT);
	}

	@Transactional(readOnly = true)
	public List<ChatRoomDto> findAllChatRoom(String email, String T) {
		Member member = memberRepository.findByEmailFJ(email)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));

		List<ChatRoom> chatRooms;
		if (T.equals("M")) {
			chatRooms = chatRoomRepository.findAllByClientId(member.getUserId())
					.orElse(new ArrayList<>());
		} else {
			chatRooms = chatRoomRepository.findAllByCordiId(member.getUserId())
					.orElse(new ArrayList<>());
		}

		return chatRooms.stream()
				.map(ChatRoom::toDto)
				.collect(Collectors.toList());
	}

	public Page<ChatMessageRes> findAllChats(Long chatRoomSeq, Pageable pageRequest) {
		return chatMessageRepository.findAllByChatRoomSeq(chatRoomSeq, pageRequest)
				.map(ChatMessage::toDto);
	}
}
