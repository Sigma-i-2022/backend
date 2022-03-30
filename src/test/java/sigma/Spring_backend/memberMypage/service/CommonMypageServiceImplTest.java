package sigma.Spring_backend.memberMypage.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.memberMypage.dto.CommonUpdateInfoReq;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;
import sigma.Spring_backend.memberMypage.repository.CommonMypageRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommonMypageServiceImplTest {

	@InjectMocks
	private CommonMypageServiceImpl commonMypageServiceImpl;
	@Mock
	private CommonMypageRepository commonMypageRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private AwsService awsService;
	@Mock
	private CommonMypage originMypage;

	private MultipartFile multipartFile;
	private CommonUpdateInfoReq updateInfoReq;
	private Member member;

	@BeforeEach
	public void setUp() throws IOException {
		member = Member.builder()
				.seq(1L)
				.email("test@test.com")
				.signupType("E")
				.password("test1234!")
				.userId("testtest")
				.updateDate(new DateConfig().getNowDate())
				.registDate(new DateConfig().getNowDate())
				.build();

		updateInfoReq = new CommonUpdateInfoReq();
		updateInfoReq.setUserId(member.getUserId());
		updateInfoReq.setEmail(member.getEmail());
		updateInfoReq.setIntro("update intro");

		InputStream is = new ClassPathResource("mock/image/icecream.png").getInputStream();
		multipartFile = new MockMultipartFile("아이스크림 사진", "icecream.png", "image/png", is);

		originMypage = CommonMypage.builder()
				.email(member.getEmail())
				.profileImgUrl("")
				.seq(member.getSeq())
				.userId(member.getUserId())
				.intro("origin intro")
				.build();
	}

	@Test
	@DisplayName("마이페이지 정보 수정")
	void updateMemberMypage() {
		member.setMypage(originMypage);
		given(memberRepository.findByEmailFJ(member.getEmail())).willReturn(Optional.of(member));

		// when
		commonMypageServiceImpl.updateClientMypageInfo(updateInfoReq);

		//then
		assertEquals(
				"update intro",
				commonMypageServiceImpl.getClientMypage(member.getEmail()).getIntro()
		);
	}

	@Test
	@DisplayName("마이페이지 소개란 500자 초과 실패")
	void myPageIntroLength500() {
		updateInfoReq.setIntro("@Test\n" +
				"\t@DisplayName(\"마이페이지 소개란 500자 초과 실패\")\n" +
				"\tvoid myPageIntroLength500() {\n" +
				"\t\tgiven(memberMypageRepository.existsByEmail(mypageReq.get(\"email\"))).willReturn(Boolean.FALSE);\n" +
				"\t\tgiven(memberRepository.existsByEmail(mypageReq.get(\"email\"))).willReturn(Boolean.TRUE);\n" +
				"\t\tgiven(mypageReq.get(\"intro\")).willReturn(\"\");\n" +
				"\t\twhen(mypageReq.get(\"intro\").length()).thenReturn(501);\n" +
				"\n" +
				"//\t\torg.junit.jupiter.api.Assertions.assertEquals(mypageReq.get(\"intro\").length(), 501);\n" +
				"\n" +
				"\t\torg.junit.jupiter.api.Assertions.assertThrows(BussinessException.class,\n" +
				"\t");
		given(commonMypageRepository.findByEmail(updateInfoReq.getEmail()))
				.willReturn(Optional.of(originMypage));

		assertNotEquals(updateInfoReq.getIntro(),
				commonMypageRepository.findByEmail(updateInfoReq.getEmail()).get().getIntro());

		assertEquals("origin intro",
				commonMypageRepository.findByEmail(updateInfoReq.getEmail()).get().getIntro());
	}

	@Test
	@DisplayName("마이페이지 프로필 업로드 성공")
	void registMemberProfileImage() {
		// given
		given(memberRepository.existsByEmail(member.getEmail()))
				.willReturn(Boolean.TRUE);
		given(commonMypageRepository.findByEmail(member.getEmail()))
				.willReturn(Optional.of(originMypage));
		given(awsService.imageUploadToS3("/profileImage", multipartFile))
				.willReturn("test_url");

		// when
		commonMypageServiceImpl.updateProfileImg(member.getEmail(), "");

		// then
		assertEquals(
				commonMypageRepository
						.findByEmail(member.getEmail())
						.get()
						.getProfileImgUrl()
				, "test_url"
		);
	}
}