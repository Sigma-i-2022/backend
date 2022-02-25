package sigma.Spring_backend.memberLook.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Keyword {

	WARM("따뜻")
	, COOL("시원")
	, MINIMAL("미니멀")
	, CASUAL("캐쥬얼")
	, VISUAL("비쥬얼")
	, THIN("날씬")
	, SHARP("샤프");

	private final String keyword;
}
