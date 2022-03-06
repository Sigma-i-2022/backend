package sigma.Spring_backend.memberLook.service;

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
import sigma.Spring_backend.memberLook.dto.Keyword;
import sigma.Spring_backend.memberLook.dto.MemberLookPageReq;
import sigma.Spring_backend.memberLook.dto.MemberLookPageRes;
import sigma.Spring_backend.memberLook.entity.MemberLookPage;
import sigma.Spring_backend.memberLook.repository.MemberLookPageRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class MemberLookServiceTest {

	@InjectMocks
	private MemberLookService memberLookService;
	@Mock
	private MemberLookPageRepository memberLookPageRepo;
	@Mock
	private MemberRepository memberRepository;
	@Mock
	private AwsService awsService;

	private static MultipartFile multipartFile;
	private static MemberLookPage memberLookPage;
	private static MemberLookPageReq memberLookPageReq;
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
				.updateDate(LocalDateTime.now())
				.registDate(LocalDateTime.now())
				.build();

		memberLookPageReq = MemberLookPageReq.builder()
				.memberEmail(member.getEmail())
				.imageFile(multipartFile)
				.explanation("test explanation")
				.shoeInfo("test shoe")
				.bottomInfo("test bottom")
				.topInfo("test top")
				.modelWeight("test weight")
				.modelHeight("test height")
				.keyword1(Keyword.CASUAL)
				.keyword2(Keyword.THIN)
				.keyword3(Keyword.WARM)
				.build();

		memberLookPage = MemberLookPage.builder()
				.seq(1L)
				.member(member)
				.imagePathUrl("test image url")
				.explanation("test explanation")
				.shoeInfo("test shoe")
				.bottomInfo("test bottom")
				.topInfo("test top")
				.modelWeight("test weight")
				.modelHeight("test height")
				.keyword1(Keyword.CASUAL)
				.keyword2(Keyword.THIN)
				.keyword3(Keyword.WARM)
				.registDate(LocalDateTime.now())
				.updateDate(LocalDateTime.now())
				.build();
	}

	@Test
	@DisplayName("회원 룩페이지 등록")
	void registLookPage() {
		//given
		given(awsService.imageUploadToS3("/memberLookImage", multipartFile)).willReturn("test aws url");
		given(memberRepository.findByEmailFJ(memberLookPageReq.getMemberEmail()))
				.willReturn(Optional.of(member));

		// when
		memberLookService.registLookPage(memberLookPageReq);

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
		given(memberLookPageRepo.findById(1L)).willReturn(Optional.of(memberLookPage));

		// when
		MemberLookPageRes lookPage = memberLookService.getLookPage(1L);

		// then
		verify(memberLookPageRepo, only()).findById(1L);
		Assertions.assertThat(lookPage)
				.isEqualTo(memberLookPage.toDto());
	}

	@Test
	@DisplayName("회원 전체 룩 페이지 조회")
	void getLookPages() {
		//given
		member.addLookPage(memberLookPage);
		member.addLookPage(MemberLookPage.builder().seq(2L).build());
		member.addLookPage(MemberLookPage.builder().seq(3L).build());
		member.addLookPage(MemberLookPage.builder().seq(4L).build());
		given(memberRepository.findByEmailFJ(member.getEmail()))
				.willReturn(Optional.of(member));

		//when
		List<MemberLookPageRes> lookPages = memberLookService.getLookPages(member.getEmail());

		//then
		Assertions.assertThat(lookPages.size()).isEqualTo(4);
		for (MemberLookPage page : member.getPages()) {
			Assertions.assertThat(page.getMember()).isEqualTo(member);
		}
	}

	@Test
	@DisplayName("회원 룩 페이지 정보 수정")
	void updateLookPageInfo() {
		//given
		given(memberLookPageRepo.findById(1L)).willReturn(Optional.of(memberLookPage));

		//when
		memberLookPageReq.setExplanation("변경된 룩 페이지 설명");
		memberLookPageReq.setTopInfo("변경된 상의 설명");
		memberLookService.updateLookPageInfo(1L, memberLookPageReq);

		//then
		Assertions.assertThat(memberLookService.getLookPage(1L).getLookPageSeq())
				.isEqualTo(1L);
		Assertions.assertThat(memberLookService.getLookPage(1L).getExplanation())
				.isEqualTo("변경된 룩 페이지 설명");
		Assertions.assertThat(memberLookService.getLookPage(1L).getTopInfo())
				.isEqualTo("변경된 상의 설명");
	}

	@Test
	@DisplayName("회원 룩 페이지 이미지 수정")
	void updateLookPageImage() {
		//given
		given(memberLookPageRepo.findById(1L)).willReturn(Optional.of(memberLookPage));
		given(awsService.imageUploadToS3("/memberLookImage", multipartFile))
				.willReturn("aws test url");

		//when
		memberLookService.updateLookPageImage(1L, multipartFile);

		//then
		Assertions.assertThat(memberLookService.getLookPage(1L).getImagePathUrl())
				.isEqualTo("aws test url");
	}

	@Test
	@DisplayName("회원 룩 페이지 삭제")
	void deleteLookPage() {
		//given
		given(memberLookPageRepo.findById(1L)).willReturn(Optional.of(memberLookPage));

		//when
		memberLookService.deleteLookPage(1L);

		//then
		org.junit.jupiter.api.Assertions.assertThrows(
				NullPointerException.class,
				() -> memberLookPage.getMember().getPages().size());
		Assertions.assertThat(memberLookPageRepo.count())
				.isEqualTo(0);
	}
	//given

	//when

	//then
}