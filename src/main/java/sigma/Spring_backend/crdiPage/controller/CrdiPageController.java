package sigma.Spring_backend.crdiPage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.crdiPage.dto.CrdiProfileReq;
import sigma.Spring_backend.crdiPage.service.CrdiPageService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "4.코디 관련 페이지")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/crdi")
public class CrdiPageController {

    private final ResponseService responseService;
    private final CrdiPageService crdiPageService;

    @PostMapping("/mypage")
    @ApiOperation(value = "코디네이터 마이페이지 등록", notes = "코디네이터의 마이페이지를 등록합니다.")
    public CommonResult crdiMypageRegist(
            @ApiParam(name = "코디마이페이지등록") @ModelAttribute CrdiProfileReq crdiProfileReq
    ) {
        try {
            crdiPageService.registCrdiMypage(crdiProfileReq);
            return responseService.getSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return responseService.getFailResult(
                    -1,
                    e.getMessage()
            );
        }
    }
}
