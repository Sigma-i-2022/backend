package sigma.Spring_backend.reservation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CrdiServiceSystem {
	OPEN_KAKAOTALK("오픈 카카오톡"),
	ZOOM("Zoom");

	private final String serviceSystemName;
}
