package sigma.Spring_backend.memberMypage.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class MemberProfileImgReq {
	private String memberEmail;
	private MultipartFile memberImageFile;
}
