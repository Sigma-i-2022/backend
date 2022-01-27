package sigma.Spring_backend.service.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sigma.Spring_backend.advice.exception.MemberEmailExistException;
import sigma.Spring_backend.advice.exception.MemberNotFoundException;
import sigma.Spring_backend.dto.member.MemberRequestDto;
import sigma.Spring_backend.dto.member.MemberResponseDto;
import sigma.Spring_backend.entity.member.Member;
import sigma.Spring_backend.repository.member.MemberRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

    public MemberResponseDto findById(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(MemberNotFoundException::new)
                .toDto();
    }

    public MemberResponseDto findByEmail(String email) {
        return memberRepository.findByEmail(email)
                .orElseThrow(MemberNotFoundException::new)
                .toDto();
    }

    public MemberResponseDto findByNameAndAddress(String name, String address) {
        return memberRepository.findByNameAndAddress(name, address)
                .orElseThrow(MemberNotFoundException::new)
                .toDto();
    }

    public List<MemberResponseDto> findAllByName(String name) {
        return memberRepository.findAllByName(name)
                .orElseThrow(MemberNotFoundException::new)
                .stream()
                .map(Member::toDto)
                .collect(Collectors.toList());
    }

    public List<MemberResponseDto> findAll() {
        return memberRepository.findAll()
                .stream()
                .map(Member::toDto)
                .collect(Collectors.toList());
    }

    public MemberResponseDto saveMember(MemberRequestDto memberRequestDto) {
        if (memberRepository.findByEmail(memberRequestDto.getEmail()).isEmpty())
            return memberRepository.save(memberRequestDto.toEntity()).toDto();
        throw new MemberEmailExistException();
    }

    @Transactional
    public MemberResponseDto updateMember(MemberRequestDto memberRequestDto) {
        Optional<Member> member = memberRepository.findByEmail(memberRequestDto.getEmail());
        if (member.isPresent()) {
            member.get().setAddress(memberRequestDto.getAddress());
            member.get().setAge(memberRequestDto.getAge());
            return member.get().toDto();
        }
        throw new MemberNotFoundException();
    }
}
