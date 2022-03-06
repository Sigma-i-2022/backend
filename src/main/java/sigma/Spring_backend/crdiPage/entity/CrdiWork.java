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

    @Column
    @Builder.Default
    private String explanation = "";

    @Column
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
    private String registDate;

    @Column
    private String updateDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "work_seq")
    private Member member;

    public Member getMember() {return this.member;}

    public void setMember(Member member) {this.member = member;}

    public CrdiWorkRes toDto() {
        return CrdiWorkRes.builder()
                .crdiWorkSeq(seq)
                .explanation(explanation)
                .imagePathUrl(imagePathUrl)
                .weight(weight)
                .height(height)
                .topInfo(topInfo)
                .bottomInfo(bottomInfo)
                .shoeInfo(shoeInfo)
                .registDate(registDate)
                .updateDate(updateDate)
                .build();
    }
}
