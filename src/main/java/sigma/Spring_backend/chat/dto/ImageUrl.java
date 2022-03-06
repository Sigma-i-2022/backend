package sigma.Spring_backend.chat.dto;

import lombok.*;
import sigma.Spring_backend.chat.entity.ChatRoom;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImageUrl {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "seq", nullable = false)
	private Long seq;

	@Column
	private String url;

	@ManyToOne(fetch = FetchType.LAZY)
	private ChatRoom chatRoom;

	public void setChatRoom(ChatRoom chatRoom) {
		this.chatRoom = chatRoom;
	}
}
