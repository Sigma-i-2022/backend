package sigma.Spring_backend.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sigma.Spring_backend.advice.exception.MemberEmailExistException;
import sigma.Spring_backend.advice.exception.MemberNotFoundException;
import sigma.Spring_backend.memberSignup.MemberRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberUtilService {

    private final MemberRepository memberRepository;

    public MemberResponseDto findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new)
                .toDto();
    }

    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(Member::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        if (memberRepository.findByEmail(memberRequestDto.getEmail()).isEmpty())
            return memberRepository.save(memberRequestDto.toEntity()).toDto();
        throw new MemberEmailExistException();
    }
}
