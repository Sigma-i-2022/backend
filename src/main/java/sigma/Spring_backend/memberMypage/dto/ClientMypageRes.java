package sigma.Spring_backend.memberMypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientMypageRes {
	private Long seq;
	private String email;
	private String userId;
	private String intro;
	private String profileImgUrl;
}
