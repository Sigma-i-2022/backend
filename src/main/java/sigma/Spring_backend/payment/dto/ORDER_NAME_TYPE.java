package sigma.Spring_backend.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ORDER_NAME_TYPE {
	STYLE_FEEDBACK("스타일 피드백"), CRDI_OR_PRODUCT_RECMD("코디 또는 구매 추천");
	private final String name;
}
