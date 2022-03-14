package sigma.Spring_backend.reservation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ReservePartTimeReq {
	@ApiModelProperty(value = "요청 시작 시간", example = "HH:mm")
	private String startTime;
	@ApiModelProperty(value = "요청 종료 시간", example = "HH:mm")
	private String endTime;

	public String toString() {
		return startTime + " - " + endTime;
	}
}
