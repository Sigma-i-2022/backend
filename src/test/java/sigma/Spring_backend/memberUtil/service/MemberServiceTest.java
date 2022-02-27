package sigma.Spring_backend.memberUtil.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.dto.MemberRequestDto;
import sigma.Spring_backend.memberUtil.dto.MemberResponseDto;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

	@InjectMocks
	private MemberService memberService;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private DateConfig dateConfig;

	private Member member;
	private MemberRequestDto memberReq;

	@BeforeEach
	void setUp() {
		member = Member.builder()
				.seq(1L)
				.email("test@test.com")
				.signupType("E")
				.password("test1234!")
				.userId("testtest")
				.updateDate(dateConfig.getNowDate())
				.registDate(dateConfig.getNowDate())
				.build();
		memberReq = MemberRequestDto.builder()
				.email("test@test.com")
				.signupType("E")
				.password("test1234!")
				.userId("testtest")
				.build();
	}

	@Test
	@DisplayName("회원 저장 성공")
	void signUp() {
		memberRepository.save(memberReq.toEntity());

		verify(memberRepository).save(any());
	}

	@Test
	@DisplayName("회원 조회 성공")
	void findMember() {
		given(memberRepository.findByEmail(member.getEmail())).willReturn(Optional.of(member));

		MemberResponseDto memberDto = memberService.findByEmail(member.getEmail());

		Assertions.assertThat(memberRepository.findByEmail(member.getEmail()).get().toDto())
				.isEqualTo(memberDto);
	}

	@Test
	@DisplayName("전체 회원 조회 성공")
	void findAllMember() {
		//given
		List<Member> list = new ArrayList<>();
		list.add(member);
		given(memberRepository.findAll()).willReturn(list);

		// when
		List<MemberResponseDto> allMember = memberService.findAll();

		//then
		Assertions.assertThat(
				memberRepository.findAll().stream().map(Member::toDto).collect(Collectors.toList())
		).isEqualTo(allMember);
	}

	@Test
	@DisplayName("회원 중복 저장 실패")
	void duplicateMember() {
		given(memberRepository.existsByEmail(memberReq.getEmail())).willReturn(Boolean.TRUE);

		org.junit.jupiter.api.Assertions
				.assertThrows(BussinessException.class, () -> memberService.save(memberReq));
	}

	@Test
	@DisplayName("회원 저장 서비스 성공")
	void joinMember() {
		given(memberRepository.existsByEmail(memberReq.getEmail())).willReturn(Boolean.FALSE);

		memberService.save(memberReq);

		verify(memberRepository).save(any());
	}
}