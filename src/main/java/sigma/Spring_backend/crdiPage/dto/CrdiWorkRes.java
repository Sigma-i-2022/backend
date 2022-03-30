package sigma.Spring_backend.crdiPage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CrdiWorkRes {

    private Long crdiWorkSeq;
    private String explanation;
    private String imagePathUrl;
    private String weight;
    private String height;
    private String topInfo;
    private String bottomInfo;
    private String shoeInfo;
    private String registDate;
    private String updateDate;
}
