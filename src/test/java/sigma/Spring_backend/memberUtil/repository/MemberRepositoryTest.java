package sigma.Spring_backend.memberUtil.repository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;
import sigma.Spring_backend.memberMypage.repository.CommonMypageRepository;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;
import sigma.Spring_backend.memberSignup.repository.AuthorizeCodeRepository;
import sigma.Spring_backend.memberSignup.repository.CrdiJoinRepository;
import sigma.Spring_backend.memberUtil.entity.Member;

import java.util.Optional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MemberRepositoryTest {

	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private CommonMypageRepository mypageRepository;
	@Autowired
	private AuthorizeCodeRepository authorizeCodeRepository;
	@Autowired
	private CrdiJoinRepository crdiJoinRepository;
	private final DateConfig dateConfig = new DateConfig();

	private Member member;

	@BeforeEach
	void setUp() {
		String email = "test@test.com2";
		String id = "testtest2";

		CommonMypage mypage = CommonMypage.builder()
				.email(email)
				.userId("")
				.intro("test intro")
				.profileImgUrl("test image url")
				.build();
		AuthorizeMember authorize = AuthorizeMember.builder()
				.email(email)
				.expired(true)
				.code("PdXsj1ane")
				.build();
		mypageRepository.save(mypage);
		authorizeCodeRepository.save(authorize);

		member = Member.builder()
				.seq(1L)
				.email(email)
				.userId(id)
				.signupType("E")
				.password("test1234!")
				.updateDate(dateConfig.getNowDate())
				.registDate(dateConfig.getNowDate())
				.activateYn("Y")
				.reportedYn("N")
				.crdiYn("N")
				.build();
		member.setMypage(mypage);
		member.setAuthorizeUser(authorize);
		System.out.println("START-SAVE========================================================================");
		memberRepository.save(member);
		System.out.println("END-SAVE========================================================================");
	}

	@Test
	@DisplayName("회원 이메일 조회")
	void findByEmail() {
		// when
		System.out.println("START-QUERY========================================================================");
		memberRepository.findByEmail(member.getEmail())
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		System.out.println("END-QUERY========================================================================");

		// then
		System.out.println("START-QUERY========================================================================");
		Assertions.assertNotNull(memberRepository.findByEmail(member.getEmail()));
		System.out.println("END-QUERY========================================================================");
	}

	@Test
	@DisplayName("회원 이메일 페치 조인 조회")
	void findByEmailByFetch() {
		// then
		System.out.println("START-QUERY========================================================================");
		org.assertj.core.api.Assertions.assertThat(memberRepository.findByEmailFJ(member.getEmail()).get().getUserId())
				.isEqualTo(member.getUserId());
		Assertions.assertEquals(memberRepository.findByEmailFJ(member.getEmail()).get().getMypage().getEmail()
		, member.getEmail());
		System.out.println("END-QUERY========================================================================");
	}

	@Test
	@DisplayName("회원 조회 - 마이페이지, 권한 존재")
	void findMemberWithMypageAuthorize() {
		System.out.println("START-QUERY========================================================================");
		Member curMem = memberRepository.findByEmail(this.member.getEmail()).get();
		System.out.println("END-QUERY========================================================================");

		CommonMypage mypage = CommonMypage.builder()
				.seq(1L)
				.profileImgUrl("test url")
				.intro("hi")
				.userId(member.getUserId())
				.email(member.getEmail())
				.build();
		curMem.setMypage(mypage);

		AuthorizeMember authorizeMember = AuthorizeMember.builder()
				.seq(1L)
				.email(member.getEmail())
				.code("TeStCoDe")
				.expired(true)
				.build();
		curMem.setAuthorizeUser(authorizeMember);

		System.out.println("START-QUERY========================================================================");
		memberRepository.save(curMem);
		System.out.println("END-QUERY========================================================================");

		System.out.println("START-QUERY========================================================================");
		org.assertj.core.api.Assertions.assertThat(memberRepository.findByEmailFJ(member.getEmail()))
				.isEqualTo(Optional.of(curMem));
		System.out.println("END-QUERY========================================================================");
	}
}