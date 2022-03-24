package sigma.Spring_backend.reservation.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sigma.Spring_backend.reservation.entity.Reservation;

import javax.validation.constraints.NotBlank;
import java.util.Comparator;

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
	@ApiModelProperty(value = "코디네이터 아이디", example = "코디네이터 아이디")
	private String crdiId;

	@NotBlank
	@ApiModelProperty(value = "고객 아이디", example = "고객 아이디")
	private String clientId;

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
	private ReservePartTimeListReq reserveTimes;

	public Reservation toEntity() {
		String[] date = reserveDay.split("-");
		reserveTimes.getReservePartTimeReqs().sort(Comparator.comparing(ReservePartTimeReq::getStartTime));

		return Reservation.builder()
				.crdiId(crdiId)
				.crdiEmail(crdiEmail)
				.clientId(clientId)
				.clientEmail(clientEmail)
				.reserveDay(date[0] + "년 " + date[1] + "월 " + date[2] + "일")
				.serviceType(serviceType)
				.serviceSystem(serviceSystem)
				.requireText(requireText)
				.reserveTimes(reserveTimes.toString())
				.confirmedReserveTime(reserveTimes.getReservePartTimeReqs().get(0).toString())
				.activateYnOfClient("Y")
				.activateYnOfCrdi("Y")
				.payYn("N")
				.confirmResvYn("N")
				.confirmPayYn("N")
				.cancelYn("N")
				.reviewedYn("N")
				.build();
	}
}
