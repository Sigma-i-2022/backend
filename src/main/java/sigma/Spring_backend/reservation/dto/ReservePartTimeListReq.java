package sigma.Spring_backend.reservation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservePartTimeListReq {
	@NotBlank
	@ApiModelProperty(value = "요청 시간대")
	private List<ReservePartTimeReq> reservePartTimeReqs;

	public String toString() {
		StringBuilder times = new StringBuilder();
		reservePartTimeReqs.sort(Comparator.comparing(ReservePartTimeReq::getStartTime));
		for (ReservePartTimeReq timeReq : reservePartTimeReqs) {
			times.append(timeReq.getStartTime())
					.append(",");
		}
		return times.toString();
	}
}
