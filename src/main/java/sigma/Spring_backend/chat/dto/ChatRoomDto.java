package sigma.Spring_backend.chat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDto {
	private Long roomSeq;
	private String initYn;
	private String clientId;
	private String clientProfileImageUrl;
	private String cordiId;
	private String cordiProfileImageUrl;
	private LocalDateTime regDate;
}
