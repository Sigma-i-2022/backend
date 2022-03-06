package sigma.Spring_backend.memberUtil.entity;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import sigma.Spring_backend.chat.entity.MemberChatRoomConnection;
import sigma.Spring_backend.memberLook.entity.MemberLookPage;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;
import sigma.Spring_backend.memberSignup.entity.JoinCrdi;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;

    @Column
    private String activateYn;

    @Column
    private String crdiYn;

    public void setActivateYn(String Yn) {
        this.activateYn = Yn;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "MYPAGE_SEQ")
    private MemberMypage mypage;

    public void registMypage(MemberMypage mypage) {
        this.mypage = mypage;
    }

    public void removeMyPage() {
        this.mypage = null;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "AUTHORIZE_USER_SEQ")
    private AuthorizeMember authorizeUser;

    public void registAuthorizeUser(AuthorizeMember authorizeUser) {
        this.authorizeUser = authorizeUser;
    }

    @OneToMany(
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "member"
    )
    @Builder.Default
    private List<MemberLookPage> pages = new ArrayList<>();

    public void addLookPage(MemberLookPage memberLookPage) {
        pages.add(memberLookPage);
        memberLookPage.setMember(this);
    }

    public void removeLookPage(MemberLookPage memberLookPage) {
        memberLookPage.setActivateYn("N");
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "seq", referencedColumnName = "JOIN_SEQ")
    private JoinCrdi joinCrdi;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY)
    @Builder.Default
    private List<MemberChatRoomConnection> MemberChatRoomConnections = new ArrayList<>();

    public void enterChatRoom(MemberChatRoomConnection memberChatRoomConnection) {
        MemberChatRoomConnections.add(memberChatRoomConnection);
        memberChatRoomConnection.setMember(this);
    }

    public MemberResponseDto toDto() {
        return MemberResponseDto.builder()
                .userSeq(seq)
                .userId(userId)
                .email(email)
                .password(password)
                .signupType(signupType)
                .registDate(registDate)
                .updateDate(updateDate)
                .build();
    }
}
