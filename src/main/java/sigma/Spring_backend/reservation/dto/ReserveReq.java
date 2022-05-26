package sigma.Spring_backend.reservation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.reservation.entity.Reservation;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReserveReq {
	@NotBlank
	@ApiModelProperty(value = "코디네이터 이메일", example = "코디네이터 이메일")
	private String crdiEmail;

	@NotBlank
	@ApiModelProperty(value = "고객 이메일", example = "코디네이터 이메일")
	private String clientEmail;

	@NotBlank
	@ApiModelProperty(value = "예약 요청 날짜", example = "yyyy-MM-dd")
	private String reserveDay;

	@NotBlank
	@ApiModelProperty(value = "서비스 종류", example = "STYLE_FEEDBACK")
	private CrdiServiceType serviceType;

	@NotBlank
	@ApiModelProperty(value = "서비스 방식", example = "OPEN_KAKAOTALK")
	private CrdiServiceSystem serviceSystem;

	@NotBlank
	@ApiModelProperty(value = "요청 사항", example = "잘 부탁드립니다.")
	private String requireText;

	@NotBlank
	@ApiModelProperty(value = "예약 요청 시간대")
	private List<String> reserveTimes;

	public Reservation toEntity(String crdiId, String clientId) {
		String[] date = reserveDay.split("-");
		StringBuilder reservationTimes = new StringBuilder();
		for (String reserveTime : reserveTimes) {
			reservationTimes.append(reserveTime).append(",");
		}

		return Reservation.builder()
				.crdiEmail(crdiEmail)
				.clientEmail(clientEmail)
				.crdiId(crdiId)
				.clientId(clientId)
				.reserveDay(date[0] + "년 " + date[1] + "월 " + date[2] + "일")
				.serviceType(serviceType)
				.serviceSystem(serviceSystem)
				.requireText(requireText)
				.reserveTimes(reservationTimes.toString())
				.confirmedReserveTime(reserveTimes.get(0))
				.activateYnOfClient("Y")
				.activateYnOfCrdi("Y")
				.payYn("N")
				.confirmPayYn("N")
				.confirmResvYn("N")
				.cancelYn("N")
				.reviewedYn("N")
				.requestReservationTime(new DateConfig().getNowDate())
				.build();
	}
}
