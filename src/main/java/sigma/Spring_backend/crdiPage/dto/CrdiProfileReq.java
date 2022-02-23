package sigma.Spring_backend.crdiPage.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class CrdiProfileReq {
    private String email;
    private String userId;
    private String intro;
    private String expertYN;
    private String sTag1;
    private String sTag2;
    private String sTag3;
    private MultipartFile profileImg;
}
