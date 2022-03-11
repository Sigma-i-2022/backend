package sigma.Spring_backend.crdiPage.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.crdiPage.dto.CrdiProfileReq;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkReq;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkRes;
import sigma.Spring_backend.crdiPage.service.CrdiPageService;
import sigma.Spring_backend.memberLook.dto.MemberLookPageRes;

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
    private final int FAIL = -1;

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

    @PostMapping("/mypageUpdate")
    @ApiOperation(value = "코디네이터 마이페이지 수정", notes = "코디네이터의 마이페이지를 수정합니다.")
    public CommonResult crdiMypageUpdate(
            @ApiParam(name = "코디마이페이지등록") @ModelAttribute CrdiProfileReq crdiProfileReq
    ) {
        try {
            crdiPageService.updateCrdiMypage(crdiProfileReq);
            return responseService.getSuccessResult();
        } catch (Exception e) {
            e.printStackTrace();
            return responseService.getFailResult(
                    -1,
                    e.getMessage()
            );
        }
    }

    @PostMapping("/work")
    @ApiOperation(value = "코디네이터 작품 등록", notes = "코디네이터의 작품을 등록합니다.")
    public CommonResult registCrdiWork(
            @ApiParam(value = "코데네이터 작품 요청 객체") @ModelAttribute CrdiWorkReq crdiWorkReq
    ){
        try{
            crdiPageService.registCrdiWork(crdiWorkReq);
            return  responseService.getSuccessResult();
        }catch (Exception e){
            e.printStackTrace();;
            return responseService.getFailResult(
                    FAIL,
                    e.getMessage()
            );
        }
    }

    @GetMapping("/work")
    @ApiOperation(value = "코디네이터 작품 조회", notes = "코디네이터의 작품을 조회합니다.")
    public CommonResult getCrdiWork(
            @ApiParam(value = "코데네이터 작품 PK") @RequestParam Long workSeq
    ){
        try{
            return responseService.getSingleResult(crdiPageService.getWork(workSeq));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BussinessException(e.getMessage());
        }
    }

    @GetMapping("/works")
    @ApiOperation(value = "코디네이터 작품 전체 조회", notes = "코디네이터 작품을 전체 조회합니다.")
    public ListResult<CrdiWorkRes> getCrdiWorks(
            @ApiParam(value = "코디 이메일") @RequestParam String crdiEmail
    ) {
        try {
            return responseService.getListResult(crdiPageService.getWorks(crdiEmail));
        } catch (Exception e) {
            e.printStackTrace();
            throw new BussinessException(e.getMessage());
        }
    }
}
