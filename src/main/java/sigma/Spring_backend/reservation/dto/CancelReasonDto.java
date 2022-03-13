package sigma.Spring_backend.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelReasonDto {
	private Long reservationSeq;
	private String reason;
	private String byWho;
}
