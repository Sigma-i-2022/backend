package sigma.Spring_backend.memberUtil.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.memberUtil.dto.MemberRequestDto;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.dto.SingleResult;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.memberUtil.service.MemberService;

import java.util.List;

@Api(tags = "01. 회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/member")
public class MemberController {

    private final ResponseService responseService;
    private final MemberService memberService;
    private final int FAIL = -1;

    @ApiOperation(value = "회원 저장", notes = "이름, 이메일, 주소, 나이, 성별을 받아서 저장합니다.")
    @PostMapping
    public SingleResult<MemberResponseDto> saveMember(
            @ApiParam(value = "회원 객체", required = true)
            @ModelAttribute MemberRequestDto member
    ) {
        return responseService.getSingleResult(memberService.save(member).toDto());
    }

    @ApiOperation(value = "이메일로 회원 조회", notes = "이메일로 회원을 조회합니다.")
    @GetMapping("/email")
    public SingleResult<MemberResponseDto> findMemberByEmail(
            @ApiParam(value = "회원 이메일", required = true) @RequestParam(name = "email") String email
    ) {
        MemberResponseDto byEmail = memberService.findByEmailWithFetch(email);
        return responseService.getSingleResult(byEmail);
    }

    @ApiOperation(value = "아이디 조회", notes = "이메일로 회원의 아이디를 조회합니다.")
    @GetMapping("/id")
    public SingleResult<String> findMemberId(
            @ApiParam(value = "회원 이메일", required = true) @RequestParam String email
    ) {
        return responseService.getSingleResult(memberService.findMemberId(email));
    }

    @ApiOperation(value = "모든 회원 조회", notes = "모든 회원을 조회합니다.")
    @GetMapping("/all")
    public ListResult<MemberResponseDto> findAllMember() {
        List<MemberResponseDto> allByName = memberService.findAll();
        return responseService.getListResult(allByName);
    }

    @PostMapping("/crdi")
    @ApiOperation(value = "코디네이터로 변경", notes = "회원을 코디네이터로 변경합니다.")
    public CommonResult updateToCrdi(
            @ApiParam(value = "회원 이메일", required = true) @RequestParam(name = "email") String email
    ) {
        boolean success = memberService.changeToCrdi(email);

        if (success) {
            return responseService.getSuccessResult();
        } else return responseService.getFailResult(
                FAIL,
                "코디네이터로 변경하는데 실패했습니다."
        );
    }
}
