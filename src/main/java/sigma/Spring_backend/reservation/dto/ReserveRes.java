package sigma.Spring_backend.reservation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveRes {
	private Long seq;
	private String crdiId;
	private String clientId;
	private CrdiServiceType serviceType;
	private CrdiServiceSystem serviceSystem;
	private Integer price;
	private String reserveDay;
	private String reserveTimes;
	private String confirmedReserveTime;
	private String requireText;
	private String payYn;
	private String confirmResvYn;
	private String confirmPayYn;
	private String reviewedYn;
	private String cancelYn;
}
