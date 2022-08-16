package sigma.Spring_backend.memberMypage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class CommonUpdateInfoReq {
	@ApiModelProperty(required = true)
	private String email;
	@ApiModelProperty(required = true)
	private String userId;
	@ApiModelProperty(required = true)
	@Size(max = 500, message = "500자 이하로 작성하세요.")
	private String intro;
}
