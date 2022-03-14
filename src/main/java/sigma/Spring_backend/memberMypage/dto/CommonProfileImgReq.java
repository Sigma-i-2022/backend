package sigma.Spring_backend.memberMypage.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CommonProfileImgReq {
	private String memberEmail;
	private MultipartFile memberImageFile;
}
