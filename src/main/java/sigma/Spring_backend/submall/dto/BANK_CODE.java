package sigma.Spring_backend.submall.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BANK_CODE {
	경남은행("경남"),
	광주은행("광주"),
	KB국민은행("국민"),
	IBK기업은행("기업"),
	NH농협은행("농협"),
	단위농협("단위농협"),
	DGB대구은행("대구"),
	부산은행("부산"),
	KDB산업은행("산업"),
	새마을금고("새마을"),
	산림조합("산림"),
	Sh수협은행("수협"),
	신한은행("신한"),
	신협("신협"),
	씨티은행("씨티"),
	우리은행("우리"),
	우체국예금보험("우체국"),
	저축은행중앙회("저축"),
	전북은행("전북"),
	제주은행("제주"),
	카카오뱅크("카카오"),
	케이뱅크("케이"),
	토스뱅크("토스"),
	하나은행("하나"),
	SC제일은행("SC제일");

	private final String name;
}
