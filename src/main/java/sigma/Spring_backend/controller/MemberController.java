package sigma.Spring_backend.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.dto.member.MemberRequestDto;
import sigma.Spring_backend.dto.member.MemberResponseDto;
import sigma.Spring_backend.entity.base.ListResult;
import sigma.Spring_backend.entity.base.SingleResult;
import sigma.Spring_backend.service.base.ResponseService;
import sigma.Spring_backend.service.member.MemberService;

import java.util.List;

@Api(tags = "1. 회원")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class MemberController {

    private final ResponseService responseService;
    private final MemberService memberService;

    @ApiOperation(value = "회원 저장", notes = "이름, 이메일, 주소, 나이, 성별을 받아서 저장합니다.")
    @PostMapping("/member")
    public SingleResult<MemberResponseDto> saveMember(
            @ApiParam(value = "회원 객체", required = true)
            @RequestBody MemberRequestDto member
    ) {
        return responseService.getSingleResult(memberService.saveMember(member));
    }

    @ApiOperation(value = "회원 정보 수정", notes = "회원의 정보를 수정합니다.")
    @PutMapping("/member")
    public SingleResult<MemberResponseDto> updateMember(
            @ApiParam(required = true)
            @RequestParam String originEmail,
            @ApiParam(required = false)
            @RequestParam String newAddress,
            @ApiParam(required = false)
            @RequestParam int newAge
    ) {
        MemberResponseDto dto = memberService.findByEmail(originEmail);
        if (newAge == 0) newAge = dto.getAge();
        if (newAddress == null) newAddress = dto.getAddress();
        MemberResponseDto memberDto = memberService.updateMember(MemberRequestDto.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .age(newAge)
                .address(newAddress)
                .gender(dto.getGender())
                .build());
        return responseService.getSingleResult(memberDto);
    }

    @ApiOperation(value = "이메일로 회원 조회", notes = "이메일로 회원을 조회합니다.")
    @GetMapping("/member/search/email/{email}")
    public SingleResult<MemberResponseDto> findMemberByEmail(
            @ApiParam(required = true)
            @PathVariable("email") String email
    ) {
        MemberResponseDto byEmail = memberService.findByEmail(email);
        return responseService.getSingleResult(byEmail);
    }

    @ApiOperation(value = "이름으로 회원 조회", notes = "이름이 같은 모든 회원을 조회합니다.")
    @GetMapping("member/search/name/{name}")
    public ListResult<MemberResponseDto> findAllMemberByName(
            @ApiParam(required = true) @PathVariable("name") String memberName
    ) {
        List<MemberResponseDto> allByName = memberService.findAllByName(memberName);
        return responseService.getListResult(allByName);
    }

    @ApiOperation(value = "모든 회원 조회", notes = "모든 회원을 조회합니다.")
    @GetMapping("/members")
    public ListResult<MemberResponseDto> findAllMember() {
        List<MemberResponseDto> allByName = memberService.findAll();
        return responseService.getListResult(allByName);
    }

}
