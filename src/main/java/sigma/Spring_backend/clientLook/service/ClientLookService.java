package sigma.Spring_backend.clientLook.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.clientLook.dto.ClientLookPageReq;
import sigma.Spring_backend.clientLook.dto.ClientLookPageRes;
import sigma.Spring_backend.clientLook.entity.ClientLookPage;
import sigma.Spring_backend.clientLook.repository.ClientLookPageRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClientLookService {

	private final ClientLookPageRepository memberLookPageRepo;
	private final MemberRepository memberRepository;
	private final AwsService awsService;

	/*
		룩 페이지 등록
	 */
	@Transactional
	public void registLookPage(ClientLookPageReq clientLookPageReq, MultipartFile imageFile) {
		// 1. 입력 폼 데이터 검증
		boolean verify = verifyLookPage(clientLookPageReq, imageFile);
		if (!verify) throw new BussinessException("룩 페이지에 필요한 정보가 없습니다.");

		// 2. AWS 이미지 업로드 후 이미지 경로 받기
		String imagePathUrl = awsService.imageUploadToS3("/memberLookImage", imageFile);

		// 3. 엔티티 생성 후 DB 저장
		try {
			Member member = memberRepository.findByEmailFJ(clientLookPageReq.getMemberEmail())
					.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
			member.addLookPage(clientLookPageReq.toEntity(imagePathUrl));
		} catch (Exception e) {
			throw new BussinessException("DB 룩북 저장 실패");
		}
	}

	private boolean verifyLookPage(ClientLookPageReq clientLookPageReq, MultipartFile imageFile) {
		if (clientLookPageReq.getMemberEmail() == null || clientLookPageReq.getMemberEmail().equals("")) {
			return false;
		}
		if (imageFile == null || imageFile.isEmpty()) {
			return false;
		}
		return true;
	}

	@Transactional(readOnly = true)
	public ClientLookPageRes getLookPage(Long key) {
		ClientLookPage lookPage = memberLookPageRepo.findById(key)
				.orElseThrow(() -> new BussinessException("해당하는 룩 페이지가 없습니다."));
		return lookPage.toDto();
	}

	@Transactional(readOnly = true)
	public List<ClientLookPageRes> getLookPages(String memberEmail) {
		Optional<Member> memberOpt = memberRepository.findByEmailFJ(memberEmail);

		return memberOpt.map(m -> m.getPages()
						.stream()
						.filter(page -> page.getActivateYn().equals("Y"))
						.map(ClientLookPage::toDto)
						.collect(Collectors.toList()))
				.orElseGet(ArrayList::new);
	}

	@Transactional
	public void updateLookPageInfo(Long key, ClientLookPageReq requestLook) {
		if (key == null) throw new BussinessException("입력 값 문제");
		ClientLookPage originLook = memberLookPageRepo.findById(key)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		originLook.setTopInfo(requestLook.getTopInfo());
		originLook.setBottomInfo(requestLook.getBottomInfo());
		originLook.setShoeInfo(requestLook.getShoeInfo());
		originLook.setExplanation(requestLook.getExplanation());
		originLook.setKeyword1(requestLook.getKeyword1());
		originLook.setKeyword2(requestLook.getKeyword2());
		originLook.setKeyword3(requestLook.getKeyword3());
		originLook.setUpdateDate(new DateConfig().getNowDate());
	}

	@Transactional
	public void updateLookPageImage(Long key, MultipartFile requestImage) {
		if (key == null) throw new BussinessException("입력 값 문제");
		ClientLookPage lookPage = memberLookPageRepo.findById(key)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		String imagePathUrl = awsService.imageUploadToS3("/memberLookImage", requestImage);
		lookPage.setImagePathUrl(imagePathUrl);
		lookPage.setUpdateDate(new DateConfig().getNowDate());
		memberLookPageRepo.save(lookPage);
	}

	@Transactional
	public void deleteLookPage(Long lookSeq) {
		ClientLookPage lookPage = memberLookPageRepo.findById(lookSeq)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND));
		lookPage.getMember().removeLookPage(lookPage);
	}

	@Transactional
	public void reportLookPage(Long lookSeq, String reason) {
		memberLookPageRepo.findById(lookSeq)
				.ifPresentOrElse(
						L -> {
							L.setReportedYn("Y");
							L.setReportContent(reason);
						}, () -> {
							throw new BussinessException(ExMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
						}
				);
	}

	@Transactional(readOnly = true)
	public List<ClientLookPageRes> getAllReportedPage() {
		return memberLookPageRepo.findAll()
				.stream()
				.filter(L -> L.getReportedYn().equals("Y"))
				.map(ClientLookPage::toDto)
				.collect(Collectors.toList());
	}
}