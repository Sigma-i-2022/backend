package sigma.Spring_backend.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderNameType {
	STYLE_FEEDBACK("스타일 피드백"), CRDI_OR_PRODUCT_RECMD("코디 또는 구매 추천");
	private final String name;
}
