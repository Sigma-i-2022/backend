package sigma.Spring_backend.memberUtil.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sigma.Spring_backend.baseUtil.advice.BussinessExceptionMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.dto.MemberRequestDto;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

    public MemberResponseDto findByEmail(String email) {
        if (memberRepository.findByEmail(email).isEmpty()) {
            throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_NOT_FOUND);
        } else {
            return memberRepository.findByEmail(email).get().toDto();
        }
    }

    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(Member::toDto)
                .collect(Collectors.toList());
    }

    public MemberResponseDto save(MemberRequestDto memberRequestDto) {
        if (memberRepository.findByEmail(memberRequestDto.getEmail()).isEmpty())
            return memberRepository.save(memberRequestDto.toEntity()).toDto();
        throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_DUPLICATE);
    }
}
