package sigma.Spring_backend.memberLook.dto;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Type {
	TOP("상의")
	, BOT("하의")
	, SHO("신발")
	, ACC("악세사리");

	private final String type;
}
