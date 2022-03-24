package sigma.Spring_backend.payment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum REFUND_BANK_TYPE {
	경남은행("경남")    				// KYONGNAMBANK
	, 광주은행("광주")    				// GWANGJUBANK
	, KB국민은행("국민")    			// KOOKMIN
	, IBK기업은행("기업")    			// IBK
	, NH농협은행("농협")    			// NONGHYEOP
	, DGB대구은행("대구")    			// DAEGUBANK
	, 부산은행("부산")    				// BUSANBANK
	, 새마을금고("새마을")    			// SAEMAUL
	, Sh수협은행("수협")    			// SUHYEOP
	, 신한은행("신한")    				// SHINHAN
	, 우리은행("우리")    				// WOORI
	, 우체국예금보험("우체국")    		// POST
	, 전북은행("전북")    				// JEONBUKBANK
	, 케이뱅크("케이")    				// KBANK
	, 하나은행("하나");				// HANA

	private final String bankName;
}
