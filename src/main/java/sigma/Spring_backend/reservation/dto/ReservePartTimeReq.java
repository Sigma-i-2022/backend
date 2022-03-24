package sigma.Spring_backend.reservation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservePartTimeReq {
	@ApiModelProperty(value = "요청 시작 시간", example = "HH:mm")
	private String startTime;

	public String toString() {
		return startTime;
	}
}
