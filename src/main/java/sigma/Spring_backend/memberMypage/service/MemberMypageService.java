package sigma.Spring_backend.memberMypage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.BussinessExceptionMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberMypage.dto.MemberProfileImgReq;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberMypage.repository.MemberMypageRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberMypageService {

	private final MemberMypageRepository memberMypageRepository;
	private final MemberRepository memberRepository;
	private final AwsService awsService;

	@Transactional(readOnly = true)
	public MemberMypage getMemberProfile(String memberEmail) {
		return memberMypageRepository.findByEmail(memberEmail).orElseThrow(
				() -> new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND)
		);
	}

	@Transactional
	public void registMemberMypage(Map<String, String> memberMypage) {
		String userEmail = memberMypage.get("email");
		String userId = memberMypage.get("userId");
		String userIntro = "";
		if (memberMypage.containsKey("intro")) {
			userIntro = memberMypage.get("intro");
		}

		if (!memberRepository.existsByEmail(userEmail)) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_NOT_FOUND);
		}

		if (memberMypageRepository.existsByEmail(userEmail)) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_EXIST);
		}

		if (userIntro.length() > 500) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_INTRO_LENGTH);
		}

		try {
			MemberMypage mypage = memberMypageRepository.save(MemberMypage.builder()
					.email(userEmail)
					.userId(userId)
					.introduction(userIntro)
					.build());
			memberRepository.findByEmail(userEmail)
					.ifPresent(m -> m.registMypage(mypage));
		} catch (Exception e) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_DB);
		}
	}

	@Transactional
	public void updateMemberProfile(Map<String, String> memberProfileInfoMap) {
		String userEmail = memberProfileInfoMap.get("email");
		String userId = memberProfileInfoMap.get("userId");
		String userIntro = memberProfileInfoMap.get("intro");

		if (!memberMypageRepository.existsByEmail(userEmail)) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
		}

		MemberMypage originMypage = memberMypageRepository.findByEmail(userEmail)
				.orElseThrow(() -> new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND));

		if (!originMypage.getUserId().equals(userId)) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_NOT_FOUND);
		}

		if (originMypage.getIntroduction().equals(userIntro)) return;

		try {
			originMypage.setIntroduction(userIntro);
			memberRepository.findByEmailFJ(userEmail)
					.ifPresent(m -> m.registMypage(originMypage));
		} catch (Exception e) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_DB);
		}
	}

	@Transactional
	public void deleteMemberMypage(Map<String, String> memberProfileInfoMap) {
		String userEmail = memberProfileInfoMap.get("email");
		String userId = memberProfileInfoMap.get("userId");

		try {
			MemberMypage originProfile = memberMypageRepository.findByEmail(userEmail)
					.orElseThrow(() -> new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND));

			if (!originProfile.getUserId().equals(userId)) {
				throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_NOT_FOUND);
			}

			memberRepository.findByEmail(userEmail)
					.ifPresent(Member::removeMyPage);
		} catch (Exception e) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
		}
	}

	@Transactional
	public void registProfileImage(MemberProfileImgReq memberProfileImgReq) {

		boolean verify = verifyRequest(memberProfileImgReq);
		if (!verify) throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_IMG_FORMAT);

		try {
			MemberMypage memberMypage = memberMypageRepository.findByEmail(memberProfileImgReq.getMemberEmail())
					.orElseThrow(() -> new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND));
			String url = awsService.imageUploadToS3("/profileImage", memberProfileImgReq.getMemberImageFile());
			memberMypage.setProfileImgUrl(url);
		} catch (Exception e) {
			throw new BussinessException("DB 회원 프로필 이미지 url 저장 실패");
		}
	}

	private boolean verifyRequest(MemberProfileImgReq request) {
		String email = request.getMemberEmail();
		MultipartFile imageFile = request.getMemberImageFile();

		if (email == null || !memberRepository.existsByEmail(email)) {
			return false;
		}
		if (imageFile == null || imageFile.isEmpty() || imageFile.getSize() == 0) {
			return false;
		}
		return true;
	}
}

