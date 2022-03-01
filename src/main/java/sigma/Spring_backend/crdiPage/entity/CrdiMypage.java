package sigma.Spring_backend.crdiPage.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CrdiMypage {
    @Id
    @Column(name = "CRDIMYPAGE_SEQ", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "crdiMypage_email", unique = true, nullable = false)
    private String email;

    @Column(unique = true, nullable = false)
    private String userId;

    @Column(length = 500)
    @Builder.Default
    private String introduction = "";

    @Column(nullable = false)
    private String expertYN;

    @Column(nullable = false)
    private String sTag1;

    @Column
    private String sTag2;

    @Column
    private String sTag3;

    @Column
    @Builder.Default
    private String profileImgUrl = "";
}
