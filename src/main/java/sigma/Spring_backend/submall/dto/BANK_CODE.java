package sigma.Spring_backend.submall.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BANK_CODE {
	경남은행("KYONGNAMBANK"),
	광주은행("GWANGJUBANK"),
	KB국민은행("KOOKMIN"),
	IBK기업은행("IBK"),
	NH농협은행("NONGHYEOP"),
	단위농협("LOCALNONGHYEOP"),
	DGB대구은행("DAEGUBANK"),
	부산은행("BUSANBANK"),
	KDB산업은행("KDB"),
	새마을금고("SAEMAUL"),
	산림조합("SANLIM"),
	Sh수협은행("SUHYEOP"),
	신한은행("SHINHAN"),
	신협("SHINHYUP"),
	씨티은행("CITI"),
	우리은행("WOORI"),
	우체국예금보험("POST"),
	저축은행중앙회("SAVINGBANK"),
	전북은행("JEONBUKBANK"),
	제주은행("JEJUBANK"),
	카카오뱅크("KAKAOBANK"),
	케이뱅크("KBANK"),
	토스뱅크("TOSSBANK"),
	하나은행("HANA"),
	SC제일은행("SC");

	private final String name;
}
