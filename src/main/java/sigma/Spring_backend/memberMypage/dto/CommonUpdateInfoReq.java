package sigma.Spring_backend.memberMypage.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommonUpdateInfoReq {
	@NotBlank
	private String email;
	@NotBlank
	private String userId;
	@Size(max = 500, message = "500자 이하로 작성하세요.")
	private String intro;
}
