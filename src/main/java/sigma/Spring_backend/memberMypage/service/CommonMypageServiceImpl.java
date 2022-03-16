package sigma.Spring_backend.memberMypage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberMypage.dto.*;
import sigma.Spring_backend.memberMypage.entity.CommonMypage;
import sigma.Spring_backend.memberMypage.repository.CommonMypageRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommonMypageServiceImpl implements CommonMypageServiceInterface {

	private final CommonMypageRepository commonMypageRepository;
	private final MemberRepository memberRepository;
	private final AwsService awsService;

	@Override
	@Transactional(readOnly = true)
	public ClientMypageRes getClientMypage(String email) {
		Member member = memberRepository.findByEmailFJ(email)
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
		return member.getMypage().toClientDto();
	}

	@Override
	@Transactional
	public void updateClientMypageInfo(CommonUpdateInfoReq updateInfoReq) {
		memberRepository.findByEmailFJ(updateInfoReq.getEmail())
				.ifPresentOrElse(
						M -> {
							M.getMypage().setIntro(updateInfoReq.getIntro());
						}, () -> {
							throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);
						}
				);
	}

	@Override
	@Transactional(readOnly = true)
	public CrdiMypageRes getCrdiMypage(String email) {
		return memberRepository.findByEmailFJ(email)
				.filter(c -> c.getCrdiYn().equals("Y"))
				.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND))
				.getMypage()
				.toCrdiDto();
	}

	@Override
	@Transactional
	public void registCrdiMypage(CrdiMypageReq crdiProfileReq, MultipartFile profileImg) {
		boolean verify = verifyCrdiRequest(profileImg);
		if (!verify) throw new BussinessException(ExMessage.MEMBER_MYPAGE_IMG_FORMAT);

		try {
			String url = awsService.imageUploadToS3("/profileImage", profileImg);
			CommonMypage mypage = memberRepository.findByEmailFJ(crdiProfileReq.getEmail())
					.filter(c -> c.getCrdiYn().equals("Y"))
					.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND))
					.getMypage();
			mypage.setEmail(crdiProfileReq.getEmail());
			mypage.setUserId(crdiProfileReq.getUserId());
			mypage.setIntro(crdiProfileReq.getIntro());
			mypage.setExpertYN(crdiProfileReq.getExpertYN());
			mypage.setSTag1(crdiProfileReq.getSTag1());
			mypage.setSTag2(crdiProfileReq.getSTag2());
			mypage.setSTag3(crdiProfileReq.getSTag3());
			mypage.setProfileImgUrl(url);
		} catch (Exception e) {
			throw new BussinessException(ExMessage.MEMBER_MYPAGE_ERROR_DB);
		}
	}

	@Override
	@Transactional
	public void updateCrdiMypageInfo(CommonUpdateInfoReq updateInfoReq) {
		memberRepository.findByEmail(updateInfoReq.getEmail())
				.ifPresentOrElse(
						M -> {
							M.getMypage().setIntro(updateInfoReq.getIntro());
						}, () -> {
							throw new BussinessException(ExMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
						}
				);
	}

	@Override
	@Transactional
	public void updateProfileImg(String email, MultipartFile imageFile) {
		boolean verify = verifyClientRequest(email, imageFile);
		if (!verify) throw new BussinessException(ExMessage.MEMBER_MYPAGE_IMG_FORMAT);

		try {
			String url = awsService.imageUploadToS3("/profileImage", imageFile);
			commonMypageRepository
					.findByEmail(email)
					.ifPresentOrElse(
							mypage -> mypage.setProfileImgUrl(url)
							, () -> {
								throw new BussinessException(ExMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
							}
					);
		} catch (Exception e) {
			throw new BussinessException("DB 회원 프로필 이미지 url 저장 실패");
		}
	}


	private boolean verifyCrdiRequest(MultipartFile imageFile) {
		if (imageFile.isEmpty() || imageFile.getSize() == 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean verifyClientRequest(String email, MultipartFile imageFile) {
		if (email == null || !memberRepository.existsByEmail(email)) {
			return false;
		}
		if (imageFile == null || imageFile.isEmpty() || imageFile.getSize() == 0) {
			return false;
		}
		return true;
	}
}

