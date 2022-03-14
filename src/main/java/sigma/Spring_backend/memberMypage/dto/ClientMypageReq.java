package sigma.Spring_backend.memberMypage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientMypageReq {
	@NotBlank
	private String email;
	@NotBlank
	private String userId;
	@Size(max = 500, message = "500자 이하로 작성하세요.")
	private String intro;
	private MultipartFile profileImg;
}
