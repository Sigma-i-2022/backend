package sigma.Spring_backend.crdiBlock.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.crdiBlock.service.CrdiBlockService;

@Api(tags = "11. 코디차단")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/block")
public class CrdiBlockController {

    private final ResponseService responseService;
    private final CrdiBlockService crdiBlockService;
    private final int FAIL = -1;

    @PostMapping
    @ApiOperation(value = "코디네이터 차단", notes = "회원이 코디네이터 차단")
    public CommonResult blockCrdi(
            @ApiParam(value = "회원 이메일", required = true) @RequestParam(name = "email") String email,
            @ApiParam(value = "코디 이메일", required = true) @RequestParam(name = "crdiEmail") String crdiEmail
    ) {

        boolean success = crdiBlockService.blockCrdi(email,crdiEmail);

        if (success) {
            return responseService.getSuccessResult();
        } else return responseService.getFailResult(
                FAIL,
                "코디네이터 차단에 실패하였습니다."
        );
    }
}
