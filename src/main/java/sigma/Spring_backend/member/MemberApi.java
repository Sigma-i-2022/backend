package sigma.Spring_backend.member;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.entity.base.ListResult;
import sigma.Spring_backend.entity.base.SingleResult;
import sigma.Spring_backend.service.base.ResponseService;

import java.util.List;

@Api(tags = "1. 회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MemberApi {

    private final ResponseService responseService;
    private final MemberUtilService memberUtilService;

    @ApiOperation(value = "회원 저장", notes = "이름, 이메일, 주소, 나이, 성별을 받아서 저장합니다.")
    @PostMapping("/member")
    public SingleResult<MemberResponseDto> saveMember(
            @ApiParam(value = "회원 객체", required = true)
            @RequestBody MemberRequestDto member
    ) {
        return responseService.getSingleResult(memberUtilService.saveMember(member));
    }

    @ApiOperation(value = "이메일로 회원 조회", notes = "이메일로 회원을 조회합니다.")
    @GetMapping("/member/{email}")
    public SingleResult<MemberResponseDto> findMemberByEmail(
            @ApiParam(required = true)
            @PathVariable("email") String email
    ) {
        MemberResponseDto member = memberUtilService.findByEmail(email);
        return responseService.getSingleResult(member);
    }

    @ApiOperation(value = "모든 회원 조회", notes = "모든 회원을 조회합니다.")
    @GetMapping("/members")
    public ListResult<MemberResponseDto> findAllMember() {
        List<MemberResponseDto> members = memberUtilService.findAll();
        return responseService.getListResult(members);
    }

}
