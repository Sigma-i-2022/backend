package sigma.Spring_backend.memberMypage.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberMypage.repository.MemberMypageRepository;
import sigma.Spring_backend.memberMypage.service.MemberMypageService;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.io.InputStream;
import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class MemberMypageControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ResponseService responseService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private MemberMypageService memberMypageService;
	@Autowired
	private MemberMypageRepository memberMypageRepository;
	@Mock
	private AwsService awsService;

	private MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	private MemberMypage mypage1;
	private Member member;

	@BeforeEach
	public void setUp() {
		mypage1 = MemberMypage.builder()
				.seq(1L)
				.email("test@test.com1")
				.introduction("test intro1")
				.profileImgUrl("test image url1")
				.userId("testtest1")
				.build();
		member = Member.builder()
				.seq(1L)
				.email("test@test.com1")
				.signupType("E")
				.password("test1234!")
				.userId("testtest")
				.updateDate(LocalDateTime.now())
				.registDate(LocalDateTime.now())
				.build();
		member.registMypage(mypage1);
		memberRepository.save(member);
//		memberMypageRepository.save(mypage1);
		mvc = MockMvcBuilders
				.standaloneSetup(new MemberMypageController(responseService, memberMypageService))
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.build();
	}

	@Test
	@DisplayName("회원 마이페이지 조회")
	void memberMypageGet() throws Exception {
		ResultActions actions = mvc.perform(
				get("/v1/api/mypage")
						.param("memberEmail", mypage1.getEmail())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		);
		actions
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.code").value(1))
				.andExpect(jsonPath("$.message").value("성공"))
				.andExpect(jsonPath("$.data.seq").value(1))
				.andExpect(jsonPath("$.data.email").value(mypage1.getEmail()));
	}

	@Test
	@DisplayName("회원 마이페이지 등록")
	@Disabled("회원 가입시 마이페이지 자동 등록 로직으로 변경되어서 필요 X")
	void memberMypageRegist() throws Exception {
		member.removeMyPage();
		memberMypageRepository.deleteByEmail(mypage1.getEmail());

		params.add("memberEmail", mypage1.getEmail());
		params.add("memberId", mypage1.getUserId());
		params.add("memberIntroPart", "Hi~ this is test intro");

		ResultActions actions = mvc.perform(
				post("/v1/api/mypage")
						.params(params)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		);
		actions
				.andDo(print())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.code").value(1))
				.andExpect(jsonPath("$.message").value("성공"));
	}

	@Test
	@DisplayName("회원 마이페이지 수정")
	void memberMypageUpdate() throws Exception {
		params.add("memberEmail", mypage1.getEmail());
		params.add("memberId", mypage1.getUserId());
		params.add("memberIntroPart", "this is updated intro");

		ResultActions actions = mvc.perform(
				put("/v1/api/mypage")
						.params(params)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		);
		actions
				.andDo(print())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.code").value(1))
				.andExpect(jsonPath("$.message").value("성공"));
	}

	@Test
	@DisplayName("회원 마이페이지 이미지 등록")
	void registMemberProfileImage() throws Exception {
		InputStream is = new ClassPathResource("mock/image/icecream.png").getInputStream();
		MultipartFile multipartFile = new MockMultipartFile("아이스크림 사진", "icecream.png", "image/png", is);
		MockMultipartFile mockMultipartFile = new MockMultipartFile("memberImageFile", "memberMypageProfileImage.jpeg", "image/png", multipartFile.getInputStream());

		BDDMockito.given(awsService.imageUploadToS3("/profileImage", mockMultipartFile)).willReturn("test aws url");

		mvc
				.perform(multipart("/v1/api/profileImage")
						.file(mockMultipartFile)
						.param("memberEmail", mypage1.getEmail())
						.contentType(MediaType.MULTIPART_FORM_DATA)
						.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.code").value(1))
				.andExpect(jsonPath("$.message").value("성공"));

	}
}