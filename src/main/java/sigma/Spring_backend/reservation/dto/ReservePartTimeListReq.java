package sigma.Spring_backend.reservation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import java.time.format.DateTimeFormatter;
import java.util.*;

@Data
public class ReservePartTimeListReq {
	@ApiModelProperty(value = "요청 시간대")
	private List<ReservePartTimeReq> reservePartTimeReqs = new ArrayList<>();

	public String toString() {
		StringBuilder times = new StringBuilder();
		reservePartTimeReqs.sort(Comparator.comparing(ReservePartTimeReq::getStartTime));
		for (ReservePartTimeReq timeReq : reservePartTimeReqs) {
			times.append(timeReq.getStartTime())
					.append(" - ")
					.append(timeReq.getEndTime())
					.append("\n");
		}
		return times.toString();
	}
}
