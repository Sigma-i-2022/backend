package sigma.Spring_backend.crdiPage.dto;

import lombok.*;

import java.util.List;

@Data
public class CrdiRes {

    private String imagePathUrl;
    private String id;
    private String sTag1;
    private String sTag2;
    private String sTag3;
    private int star;
    private List<String> imageWorkImageList;

}
