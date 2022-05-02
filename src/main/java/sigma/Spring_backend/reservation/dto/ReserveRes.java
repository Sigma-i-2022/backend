package sigma.Spring_backend.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReserveRes {
	private Long seq;
	private String crdiId;
	private String crdiEmail;
	private String clientId;
	private String clientEmail;
	private CrdiServiceType serviceType;
	private CrdiServiceSystem serviceSystem;
	private Integer price;
	private String reserveDay;
	private List<String> reserveTimes;
	private String confirmedReserveTime;
	private String requireText;
	private String payYn;
	private String confirmResvYn;
	private String confirmPayYn;
	private String reviewedYn;
	private String cancelYn;
	private String requestReservationTime;
}
