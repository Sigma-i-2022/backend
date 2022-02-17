package sigma.Spring_backend.member.dto;

import lombok.Getter;
import sigma.Spring_backend.memberUtil.entity.Member;


import java.io.Serializable;
@Getter
public class MemberSessionDto implements Serializable {
    private String userId;
    private String email;
    private String password;


    public MemberSessionDto(Member member) {
        this.userId = member.getUserId();
        this.email = member.getEmail();
        this.password = member.getPassword();
    }
}