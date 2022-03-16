package sigma.Spring_backend.reservation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CancelReasonDto {
	@NotBlank
	private Long reservationSeq;
	@NotBlank
	private String reason;
	@NotBlank
	@Enumerated(EnumType.STRING)
	private TYPE byWho;
}
