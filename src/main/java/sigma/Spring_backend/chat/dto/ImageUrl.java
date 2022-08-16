package sigma.Spring_backend.chat.dto;

import lombok.*;
import sigma.Spring_backend.chat.entity.ChatRoom;

import javax.persistence.*;

@Entity
@Getter
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

	@Setter
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHAT_ROOM_SEQ")
	private ChatRoom chatRoom;
}
