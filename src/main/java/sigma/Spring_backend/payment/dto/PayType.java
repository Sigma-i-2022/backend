package sigma.Spring_backend.payment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PayType {
	CARD("카드");
	private final String name;
}
