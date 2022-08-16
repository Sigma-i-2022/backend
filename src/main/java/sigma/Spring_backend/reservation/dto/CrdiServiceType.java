package sigma.Spring_backend.reservation.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum CrdiServiceType {
	STYLE_FEEDBACK("스타일 피드백"),
	CRDI_OR_PRODUCT_RECMD("코디 또는 상품 추천");

	private final String serviceName;
}
