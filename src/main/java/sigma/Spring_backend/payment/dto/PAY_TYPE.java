package sigma.Spring_backend.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum PAY_TYPE {
	CARD("카드"), VIRTUAL_ACCOUNT("가상계좌");

	private final String name;
}
