package sigma.Spring_backend.memberUtil.entity;

import lombok.*;
import sigma.Spring_backend.memberLook.entity.MemberLookPage;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;
import sigma.Spring_backend.memberSignup.entity.JoinCrdi;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member {
    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column
    private String signupType;

    @Column
    private String gender;

    @Column
    private int age;

    @Column
    private String registDate;

    @Column
    private String updateDate;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "mypage_seq", referencedColumnName = "SEQ")
    private MemberMypage mypage;

    public void registMypage(MemberMypage mypage) {
        this.mypage = mypage;
    }

    public void removeMyPage() {
        this.mypage.setEmail("");
        this.mypage = null;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "authorizeUser_seq", referencedColumnName = "SEQ")
    private AuthorizeMember authorizeUser;

    public void registAuthorizeUser(AuthorizeMember authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberLookPage> pages = new ArrayList<>();

    public void addLookPage(MemberLookPage memberLookPage) {
        pages.add(memberLookPage);
        memberLookPage.setMember(this);
    }

    public void removeLookPage(MemberLookPage memberLookPage) {
        pages.remove(memberLookPage);
        memberLookPage.setMember(null);
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seq", referencedColumnName = "JOIN_SEQ")
    private JoinCrdi joinCrdi;

    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .userId(userId)
                .email(email)
                .password(password)
                .signupType(signupType)
                .gender(gender)
                .age(age)
                .build();
    }
}
