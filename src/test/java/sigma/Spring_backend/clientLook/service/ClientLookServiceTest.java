package sigma.Spring_backend.clientLook.service;

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
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.clientLook.dto.Keyword;
import sigma.Spring_backend.clientLook.dto.ClientLookPageReq;
import sigma.Spring_backend.clientLook.dto.ClientLookPageRes;
import sigma.Spring_backend.clientLook.entity.ClientLookPage;
import sigma.Spring_backend.clientLook.repository.ClientLookPageRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class ClientLookServiceTest {

	@InjectMocks
	private ClientLookService clientLookService;
	@Mock
	private ClientLookPageRepository memberLookPageRepo;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private AwsService awsService;

	private static MultipartFile multipartFile;
	private static ClientLookPage clientLookPage;
	private static ClientLookPageReq clientLookPageReq;
	private static Member member;

	@BeforeEach
	void setUp() throws IOException {
		InputStream is = new ClassPathResource("mock/image/icecream.png").getInputStream();
		multipartFile = new MockMultipartFile("아이스크림 사진", "icecream.png", "image/png", is);

		member = Member.builder()
				.seq(1L)
				.email("test@test.com")
				.signupType("E")
				.password("test1234!")
				.userId("testtest")
				.updateDate(new DateConfig().getNowDate())
				.registDate(new DateConfig().getNowDate())
				.build();

		clientLookPageReq = ClientLookPageReq.builder()
				.memberEmail(member.getEmail())
				.explanation("test explanation")
				.shoeInfo("test shoe")
				.bottomInfo("test bottom")
				.topInfo("test top")
				.keyword1(Keyword.CASUAL)
				.keyword2(Keyword.THIN)
				.keyword3(Keyword.WARM)
				.build();

		clientLookPage = ClientLookPage.builder()
				.seq(1L)
				.member(member)
				.imagePathUrl("test image url")
				.explanation("test explanation")
				.shoeInfo("test shoe")
				.bottomInfo("test bottom")
				.topInfo("test top")
				.keyword1(Keyword.CASUAL)
				.keyword2(Keyword.THIN)
				.keyword3(Keyword.WARM)
				.registDate(new DateConfig().getNowDate())
				.updateDate(new DateConfig().getNowDate())
				.activateYn("Y")
				.build();
	}

	@Test
	@DisplayName("회원 룩페이지 등록")
	void registLookPage() {
		//given
		given(awsService.imageUploadToS3("/memberLookImage", multipartFile)).willReturn("test aws url");
		given(memberRepository.findByEmailFJ(clientLookPageReq.getMemberEmail()))
				.willReturn(Optional.of(member));

		// when
		clientLookService.registLookPage(clientLookPageReq, multipartFile);

		//then
		Assertions.assertThat(
				member.getPages().get(0).getMember()).isEqualTo(member);
		Assertions.assertThat(
				member.getPages().get(0).getImagePathUrl()).isEqualTo("test aws url");
	}

	@Test
	@DisplayName("회원 룩 페이지 조회")
	void getLookPage() {
		// given
		given(memberLookPageRepo.findById(1L)).willReturn(Optional.of(clientLookPage));

		// when
		ClientLookPageRes lookPage = clientLookService.getLookPage(1L);

		// then
		verify(memberLookPageRepo, only()).findById(1L);
		Assertions.assertThat(lookPage)
				.isEqualTo(clientLookPage.toDto());
	}

	@Test
	@DisplayName("회원 전체 룩 페이지 조회")
	void getLookPages() {
		//given
		member.addLookPage(clientLookPage);
		member.addLookPage(ClientLookPage.builder().seq(2L).activateYn("Y").build());
		member.addLookPage(ClientLookPage.builder().seq(3L).activateYn("Y").build());
		member.addLookPage(ClientLookPage.builder().seq(4L).activateYn("Y").build());
		given(memberRepository.findByEmailFJ(member.getEmail()))
				.willReturn(Optional.of(member));

		//when
		List<ClientLookPageRes> lookPages = clientLookService.getLookPages(member.getEmail());

		//then
		Assertions.assertThat(lookPages.size()).isEqualTo(4);
		for (ClientLookPage page : member.getPages()) {
			Assertions.assertThat(page.getMember()).isEqualTo(member);
		}
	}

	@Test
	@DisplayName("회원 룩 페이지 정보 수정")
	void updateLookPageInfo() {
		//given
		given(memberLookPageRepo.findById(1L)).willReturn(Optional.of(clientLookPage));

		//when
		clientLookPageReq.setExplanation("변경된 룩 페이지 설명");
		clientLookPageReq.setTopInfo("변경된 상의 설명");
		clientLookService.updateLookPageInfo(1L, clientLookPageReq);

		//then
		Assertions.assertThat(clientLookService.getLookPage(1L).getLookPageSeq())
				.isEqualTo(1L);
		Assertions.assertThat(clientLookService.getLookPage(1L).getExplanation())
				.isEqualTo("변경된 룩 페이지 설명");
		Assertions.assertThat(clientLookService.getLookPage(1L).getTopInfo())
				.isEqualTo("변경된 상의 설명");
	}

	@Test
	@DisplayName("회원 룩 페이지 이미지 수정")
	void updateLookPageImage() {
		//given
		given(memberLookPageRepo.findById(1L)).willReturn(Optional.of(clientLookPage));
		given(awsService.imageUploadToS3("/memberLookImage", multipartFile))
				.willReturn("aws test url");

		//when
		clientLookService.updateLookPageImage(1L, multipartFile);

		//then
		Assertions.assertThat(clientLookService.getLookPage(1L).getImagePathUrl())
				.isEqualTo("aws test url");
	}

	@Test
	@DisplayName("회원 룩 페이지 삭제")
	void deleteLookPage() {
		//given
		given(memberLookPageRepo.findById(1L)).willReturn(Optional.of(clientLookPage));

		//when
		clientLookService.deleteLookPage(1L);

		//then
		org.junit.jupiter.api.Assertions.assertEquals(
				memberLookPageRepo.findById(1L).get().getActivateYn(),
				"N"
		);
	}
	//given

	//when

	//then
}