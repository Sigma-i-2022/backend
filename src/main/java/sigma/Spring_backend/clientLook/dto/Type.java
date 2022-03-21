package sigma.Spring_backend.clientLook.dto;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Type {
	TOP("상의")
	, BOT("하의")
	, SHO("신발")
	, ACC("악세사리");

	private final String type;
}
