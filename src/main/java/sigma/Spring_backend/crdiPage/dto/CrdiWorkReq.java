package sigma.Spring_backend.crdiPage.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;

@Data
@Builder
@AllArgsConstructor
public class CrdiWorkReq {

    @ApiModelProperty(required = true)
    private String crdiEmail;

    @ApiModelProperty(required = true)
    private String explanation;

    private String weight;
    private String height;
    private String topInfo;
    private String bottomInfo;
    private String shoeInfo;
    private String keyword1;
    private String keyword2;
    private String keyword3;

    public CrdiWork toEntity(String imagePathUrl){
        return CrdiWork.builder()
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
                .activateYn("Y")
                .updateDate(new DateConfig().getNowDate())
                .registDate(new DateConfig().getNowDate())
                .build();
    }

}
