package sigma.Spring_backend.crdiPage.entity;

import lombok.*;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkRes;
import sigma.Spring_backend.memberUtil.entity.Member;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class CrdiWork {

    @Id
    @Column(unique = true, nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(unique = true, nullable = false)
    private String crdiEmail;

    @Column(nullable = false)
    private String explanation;

    @Column(nullable = false)
    private String imagePathUrl;

    @Column
    private String weight;

    @Column
    private String height;

    @Column
    private String topInfo;

    @Column
    private String bottomInfo;

    @Column
    private String shoeInfo;

    @Column
    private String keyword1;

    @Column
    private String keyword2;

    @Column
    private String keyword3;

    @Column
    private String registDate;

    @Column
    private String updateDate;

    @Column
    private String activateYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_SEQ")
    private Member member;

    public Member getMember() {return this.member;}

    public void setMember(Member member) {this.member = member;}

    public CrdiWorkRes toDto() {
        return CrdiWorkRes.builder()
                .crdiWorkSeq(seq)
                .crdiEmail(crdiEmail)
                .explanation(explanation)
                .imagePathUrl(imagePathUrl)
                .weight(weight)
                .height(height)
                .topInfo(topInfo)
                .bottomInfo(bottomInfo)
                .shoeInfo(shoeInfo)
                .keyword1(keyword1)
                .keyword2(keyword2)
                .keyword3(keyword3)
                .registDate(registDate)
                .updateDate(updateDate)
                .build();
    }
}
