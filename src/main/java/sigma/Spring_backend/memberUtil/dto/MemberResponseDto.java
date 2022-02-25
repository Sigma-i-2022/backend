package sigma.Spring_backend.memberUtil.dto;

import lombok.*;

@Data
@Builder
public class MemberResponseDto {
    private String userId;
    private String email;
    private String password;
    private String signupType;
    private String gender;
    private String registDate;
    private String updateDate;
    private int age;
}
