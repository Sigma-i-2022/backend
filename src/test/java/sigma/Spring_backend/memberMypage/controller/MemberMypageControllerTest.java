package sigma.Spring_backend.memberMypage.controller;

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
import sigma.Spring_backend.memberMypage.dto.MemberProfileImgReq;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberMypage.repository.MemberMypageRepository;
import sigma.Spring_backend.memberMypage.service.MemberMypageService;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.io.IOException;
import java.io.InputStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

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

	private MultipartFile multipartFile;

	private MemberProfileImgReq profileImgReq;

	@BeforeEach
	public void setUp() throws IOException {
		InputStream is = new ClassPathResource("mock/image/icecream.png").getInputStream();
		multipartFile = new MockMultipartFile("아이스크림 사진", "icecream.png", "image/png", is);
		profileImgReq = new MemberProfileImgReq();
		profileImgReq.setMemberEmail("test@test.com");
		profileImgReq.setMemberImageFile(multipartFile);
		memberMypage = MemberMypage.builder()
				.email("test@test.com")
				.profileImgUrl("")
				.seq(0L)
				.userId("test")
				.build();
	}

	@Test
	@DisplayName("파일 업로드 성공")
	void registMemberProfileImage() {
		// given
		given(memberRepository.existsByEmail(profileImgReq.getMemberEmail())).willReturn(true);
		given(memberMypageRepository.findByEmail(profileImgReq.getMemberEmail())).willReturn(memberMypage);
		given(awsService.imageUploadToS3("/profileImage", profileImgReq.getMemberImageFile())).willReturn("test_url");

		// when
		memberMypageService.registProfileImage(profileImgReq);

		// then
		verify(memberMypageRepository).save(any());
	}
}