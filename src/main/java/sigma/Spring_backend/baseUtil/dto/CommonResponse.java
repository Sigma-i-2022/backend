package sigma.Spring_backend.baseUtil.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommonResponse {
    SUCCESS(1, "성공"),
    FAIL(-1, "실패");

    private int code;
    private String message;
}
