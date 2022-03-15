package sigma.Spring_backend.reservation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.Spring_backend.reservation.entity.Reservation;

import java.util.Comparator;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveReq {
	@ApiModelProperty(value = "코디네이터 아이디", example = "코디네이터 아이디")
	private String crdiId;

	@ApiModelProperty(value = "고객 아이디", example = "고객 아이디")
	private String clientId;

	@ApiModelProperty(value = "예약 요청 날짜", example = "yyyy-MM-dd")
	private String reserveDay;

	@ApiModelProperty(value = "서비스 종류", example = "STYLE_FEEDBACK")
	private CrdiServiceType serviceType;

	@ApiModelProperty(value = "서비스 방식", example = "OPEN_KAKAOTALK")
	private CrdiServiceSystem serviceSystem;

	@ApiModelProperty(value = "요청 사항", example = "잘 부탁드립니다.")
	private String requireText;

	@ApiModelProperty(value = "예약 요청 시간대")
	private ReservePartTimeListReq reserveTimes;

	public Reservation toEntity() {
		String[] date = reserveDay.split("-");
		reserveTimes.getReservePartTimeReqs().sort(Comparator.comparing(ReservePartTimeReq::getStartTime));

		return Reservation.builder()
				.crdiId(crdiId)
				.clientId(clientId)
				.reserveDay(date[0] + "년 " + date[1] + "월 " + date[2] + "일")
				.serviceType(serviceType)
				.serviceSystem(serviceSystem)
				.requireText(requireText)
				.reserveTimes(reserveTimes.toString())
				.confirmedReserveTime(reserveTimes.getReservePartTimeReqs().get(0).toString())
				.activateYnOfClient("Y")
				.activateYnOfCrdi("Y")
				.confirmResvYn("N")
				.confirmPayYn("N")
				.cancelYn("N")
				.reviewedYn("N")
				.build();
	}
}