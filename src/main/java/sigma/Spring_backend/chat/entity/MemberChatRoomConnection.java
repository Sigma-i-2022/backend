package sigma.Spring_backend.chat.entity;

import lombok.*;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class MemberChatRoomConnection {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "SEQ", nullable = false)
	private Long seq;

	/*
	채팅방 : 회원 = 1 : N
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "MEMBER_SEQ")
	private Member member;

	/*
	회원 채팅방 = N : 1
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHAT_ROOM_SEQ")
	private ChatRoom chatRoom;
}
