package sigma.Spring_backend.crdiPage.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;

@Data
@Builder
@AllArgsConstructor
public class CrdiWorkReq {

    private String crdiEmail;

    @Builder.Default
    private String explanation  = "";

    private MultipartFile imageFile;
    private String weight;
    private String height;
    private String topInfo;
    private String bottomInfo;
    private String shoeInfo;

    public CrdiWork toEntity(String imagePathUrl){
        return CrdiWork.builder()
                .explanation(this.explanation)
                .imagePathUrl(imagePathUrl)
                .weight(this.weight)
                .height(this.height)
                .topInfo(this.topInfo)
                .bottomInfo(this.bottomInfo)
                .shoeInfo(this.shoeInfo)
                .updateDate(new DateConfig().getNowDate())
                .registDate(new DateConfig().getNowDate())
                .build();
    }

}
