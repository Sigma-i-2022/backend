package sigma.Spring_backend.entity.member;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {

    male("남자"),
    female("여자"),
    none("미정의");

    private String gender;
}
