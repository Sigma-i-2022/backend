package sigma.Spring_backend.memberUtil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.dto.MemberRequestDto;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public MemberResponseDto findByEmail(String email) {
        Member member = memberRepository.findByEmailFJ(email)
                .orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
        return member.toDto();
    }

    @Transactional(readOnly = true)
    public MemberResponseDto findByEmailWithFetch(String email) {
        if (!memberRepository.findByEmailFJ(email).isPresent()) {
            throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);
        } else {
            return memberRepository.findByEmailFJ(email).get().toDto();
        }
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(Member::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public Member save(MemberRequestDto memberRequestDto) {
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new BussinessException(ExMessage.MEMBER_ERROR_DUPLICATE);
        }
        return memberRepository.save(memberRequestDto.toEntity());
    }

    @Transactional
    public void deActivateUser(String email) {
        Member member = memberRepository.findByEmailFJ(email)
                .orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
        member.setActivateYn("N");
    }

    @Transactional
    public boolean changeToCrdi(String email) {
        memberRepository.findByEmailFJ(email).ifPresent(M -> M.setCrdiYn("Y"));
        return true;
    }
}
