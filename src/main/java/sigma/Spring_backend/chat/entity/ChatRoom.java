package sigma.Spring_backend.chat.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import sigma.Spring_backend.chat.dto.ChatRoomDto;
import sigma.Spring_backend.chat.dto.ImageUrl;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name ="SEQ",unique = true, nullable = false)
	private Long seq;

	@Column
	private String initYn;

	@Column
	private String clientId;

	@Column
	private String clientProfileImageUrl;

	@Column
	private String cordiId;

	@Column
	private String cordiProfileImageUrl;

	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime regDate;

	public void setInitYn(String yn) {
		this.initYn = yn;
	}

	@OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Builder.Default
	private List<ImageUrl> images = new ArrayList<>();

	public void addImage(ImageUrl imageUrl) {
		images.add(imageUrl);
		imageUrl.setChatRoom(this);
	}

	@OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Builder.Default
	private List<ChatMessage> messages = new ArrayList<>();

	public void addChatMessage(ChatMessage chatMessage) {
		messages.add(chatMessage);
		chatMessage.setChatRoom(this);
	}

	@OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@Builder.Default
	private List<MemberChatRoomConnection> MemberChatRoomConnections = new ArrayList<>();

	public void addMemberChatRoom(MemberChatRoomConnection memberChatRoomConnection) {
		MemberChatRoomConnections.add(memberChatRoomConnection);
		memberChatRoomConnection.setChatRoom(this);
	}

	public ChatRoomDto toDto() {
		return ChatRoomDto.builder()
				.roomSeq(this.seq)
				.initYn(this.initYn)
				.clientId(this.clientId)
				.clientProfileImageUrl(this.clientProfileImageUrl)
				.cordiId(this.cordiId)
				.cordiProfileImageUrl(this.cordiProfileImageUrl)
				.regDate(this.regDate)
				.build();
	}

}
