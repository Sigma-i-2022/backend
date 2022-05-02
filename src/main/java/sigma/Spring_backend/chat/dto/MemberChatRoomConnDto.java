package sigma.Spring_backend.chat.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.Spring_backend.chat.entity.ChatRoom;
import sigma.Spring_backend.chat.entity.MemberChatRoomConnection;
import sigma.Spring_backend.memberUtil.entity.Member;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberChatRoomConnDto {
	@ApiModelProperty(required = true)
	private Member member;
	@ApiModelProperty(required = true)
	private ChatRoom chatRoom;

	public MemberChatRoomConnection toEntity() {
		return MemberChatRoomConnection.builder()
				.member(this.member)
				.chatRoom(this.chatRoom)
				.build();
	}
}
