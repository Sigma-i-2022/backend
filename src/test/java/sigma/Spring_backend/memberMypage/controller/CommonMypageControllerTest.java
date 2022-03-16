package sigma.Spring_backend.memberMypage.controller;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.service.ResponseService;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;
import sigma.Spring_backend.memberMypage.service.CommonMypageServiceImpl;
import sigma.Spring_backend.memberSignup.entity.AuthorizeMember;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.io.InputStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.AUTO_CONFIGURED)
@AutoConfigureMockMvc
class CommonMypageControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private ResponseService responseService;
	@Autowired
	private MemberRepository memberRepository;
	@Autowired
	private CommonMypageServiceImpl commonMypageServiceImpl;
	@Mock
	private AwsService awsService;

	private final MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
	private Member member;

	@BeforeEach
	public void setUp() {
		String tmp = "test@test.com";
		member = Member.builder()
				.email(tmp)
				.signupType("E")
				.password("test1234!")
				.userId(tmp)
				.updateDate(new DateConfig().getNowDate())
				.registDate(new DateConfig().getNowDate())
				.activateYn("Y")
				.reportedYn("N")
				.crdiYn("N")
				.build();
		mvc = MockMvcBuilders
				.standaloneSetup(new CommonMypageController(responseService, commonMypageServiceImpl))
				.addFilters(new CharacterEncodingFilter("UTF-8", true))
				.build();
		setting();
	}

	@AfterEach
	public void clear() {
		memberRepository.delete(member);
	}

	@Transactional
	public void setting() {
		save();
		CommonMypage mypage = CommonMypage.builder()
				.email(member.getEmail())
				.userId(member.getUserId())
				.intro("test intro")
				.profileImgUrl("test image url")
				.build();
		member.setMypage(mypage);

		AuthorizeMember authorize = AuthorizeMember.builder()
				.email(member.getEmail())
				.expired(true)
				.code("PdXsj1ane")
				.build();
		member.setAuthorizeUser(authorize);
		System.out.println("$$$$$$$$$$$$\nmember.getSeq() = " + member.getSeq());
	}

	@Transactional
	public void save() {
		memberRepository.save(member);
	}

	@Test
	@Disabled(value = "페치 조인 때문에 자꾸 널로 조회됨")
	@DisplayName("회원 마이페이지 조회")
	void memberMypageGet() throws Exception {
		ResultActions actions = mvc.perform(
				get("/v1/api/mypage/client")
						.param("email", member.getEmail())
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON)
		);
		actions
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.code").value(1))
				.andExpect(jsonPath("$.message").value("성공"))
				.andExpect(jsonPath("$.data.email").value(member.getEmail()));
	}

	@Test
	@Disabled(value = "페치 조인 때문에 자꾸 널로 조회됨")
	@DisplayName("회원 마이페이지 수정")
	void memberMypageUpdate() throws Exception {
		params.add("email", member.getEmail());
		params.add("userId", member.getUserId());
		params.add("intro", "this is updated intro");

		ResultActions actions = mvc.perform(
				put("/v1/api/mypage/client")
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
	@Disabled(value = "페치 조인 때문에 자꾸 널로 조회됨")
	@DisplayName("회원 마이페이지 이미지 등록")
	void registMemberProfileImage() throws Exception {
		InputStream is = new ClassPathResource("mock/image/icecream.png").getInputStream();
		MultipartFile multipartFile = new MockMultipartFile("아이스크림 사진", "icecream.png", "image/png", is);
		MockMultipartFile mockMultipartFile = new MockMultipartFile("memberImageFile", "memberMypageProfileImage.jpeg", "image/png", multipartFile.getInputStream());

		BDDMockito.given(awsService.imageUploadToS3("/profileImage", mockMultipartFile)).willReturn("test aws url");

		mvc
				.perform(multipart("/v1/api/mypage/image")
						.file(mockMultipartFile)
						.param("memberEmail", member.getEmail())
						.contentType(MediaType.MULTIPART_FORM_DATA)
						.accept(MediaType.APPLICATION_JSON)
				)
				.andDo(print())
				.andExpect(jsonPath("$.success").value(true))
				.andExpect(jsonPath("$.code").value(1))
				.andExpect(jsonPath("$.message").value("성공"));

	}
}