package sigma.Spring_backend.memberMypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrdiMypageRes {
	private Long seq;
	private String email;
	private String userId;
	private String intro;
	private String expertYN;
	private String sTag1;
	private String sTag2;
	private String sTag3;
	private String profileImgUrl;
}
