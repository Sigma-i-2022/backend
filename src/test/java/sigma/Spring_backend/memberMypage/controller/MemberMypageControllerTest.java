package sigma.Spring_backend.memberMypage.controller;

import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
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
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberMypage.dto.MemberProfileImgReq;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberMypage.repository.MemberMypageRepository;
import sigma.Spring_backend.memberMypage.service.MemberMypageService;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MemberMypageControllerTest {

	@InjectMocks
	private MemberMypageService memberMypageService;
	@Mock
	private MemberMypageRepository memberMypageRepository;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private AwsService awsService;
	@Mock
	private MemberMypage memberMypage;
	@Mock
	private Map<String, String> mypageReq;

	private MultipartFile multipartFile;
	private MemberProfileImgReq profileImgReq;

	@BeforeEach
	public void setUp() throws IOException {
		mypageReq = new HashMap<>();
		mypageReq.put("email", "test@test.com");
		mypageReq.put("userId", "testtest");
		mypageReq.put("intro", "this is test intro");

		InputStream is = new ClassPathResource("mock/image/icecream.png").getInputStream();
		multipartFile = new MockMultipartFile("아이스크림 사진", "icecream.png", "image/png", is);

		profileImgReq = new MemberProfileImgReq();
		profileImgReq.setMemberEmail("test@test.com");
		profileImgReq.setMemberImageFile(multipartFile);

		memberMypage = MemberMypage.builder()
				.email("test@test.com")
				.profileImgUrl("")
				.seq(0L)
				.userId("testtest")
				.build();
	}

	@Test
	@DisplayName("마이페이지 생성")
	void registMemberMypage() {
		given(memberMypageRepository.existsByEmail(mypageReq.get("email"))).willReturn(Boolean.FALSE);
		given(memberRepository.existsByEmail(mypageReq.get("email"))).willReturn(Boolean.TRUE);
		given(memberRepository.findByEmail(mypageReq.get("email"))).willReturn(Optional.of(Member.builder().build()));

		memberMypageService.registMemberMypage(mypageReq);

		verify(memberMypageRepository).save(any());
	}

	@Test
	@DisplayName("마이페이지 정보 수정")
	void updateMemberMypage() {
		given(memberMypageRepository.existsByEmail(mypageReq.get("email"))).willReturn(Boolean.TRUE);
		given(memberMypageRepository.findByEmail(mypageReq.get("email"))).willReturn(Optional.of(memberMypage));
		mypageReq.put("intro", "update intro");

		// when
		memberMypageService.updateMemberProfile(mypageReq);

		//then
		Assertions.assertThat(memberMypageService.getMemberProfile(mypageReq.get("email")))
				.isEqualTo(memberMypage);
		Assertions.assertThat(memberMypageService.getMemberProfile(mypageReq.get("email")).getIntroduction())
				.isEqualTo("update intro");
	}

	@Test
	@DisplayName("마이페이지 소개란 500자 초과 실패")
	void myPageIntroLength500() {
		mypageReq.put("intro", "@Test\n" +
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
		given(memberMypageRepository.existsByEmail(mypageReq.get("email"))).willReturn(Boolean.FALSE);
		given(memberRepository.existsByEmail(mypageReq.get("email"))).willReturn(Boolean.TRUE);

		org.junit.jupiter.api.Assertions.assertThrows(BussinessException.class,
				() -> memberMypageService.registMemberMypage(mypageReq)
		);
	}

	@Test
	@DisplayName("마이페이지 프로필 업로드 성공")
	void registMemberProfileImage() {
		// given
		given(memberRepository.existsByEmail(profileImgReq.getMemberEmail()))
				.willReturn(Boolean.TRUE);
		given(memberMypageRepository.findByEmail(profileImgReq.getMemberEmail()))
				.willReturn(Optional.of(memberMypage));
		given(awsService.imageUploadToS3("/profileImage", profileImgReq.getMemberImageFile()))
				.willReturn("test_url");

		// when
		memberMypageService.registProfileImage(profileImgReq);

		// then
		Assertions.assertThat(memberMypageService.getMemberProfile(profileImgReq.getMemberEmail())
						.getProfileImgUrl()).isEqualTo("test_url");
	}
}