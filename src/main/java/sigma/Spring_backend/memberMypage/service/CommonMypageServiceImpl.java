package sigma.Spring_backend.memberMypage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.imageUtil.service.ImageService;
import sigma.Spring_backend.memberMypage.dto.ClientMypageRes;
import sigma.Spring_backend.memberMypage.dto.CommonUpdateInfoReq;
import sigma.Spring_backend.memberMypage.dto.CrdiMypageReq;
import sigma.Spring_backend.memberMypage.dto.CrdiMypageRes;
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
	private final ImageService imageService;

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
	public void registCrdiMypage(CrdiMypageReq crdiProfileReq, @Nullable String uuid) {
		try {
			CommonMypage mypage = memberRepository.findByEmailFJ(crdiProfileReq.getEmail())
					.filter(c -> c.getCrdiYn().equals("Y"))
					.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND))
					.getMypage();
			mypage.setEmail(crdiProfileReq.getEmail());
			mypage.setUserId(crdiProfileReq.getUserId());
			mypage.setIntro(crdiProfileReq.getIntro());
			mypage.setExpertYN(crdiProfileReq.getExpertYN());
			mypage.setSTag1(crdiProfileReq.getTag1());
			mypage.setSTag2(crdiProfileReq.getTag2());
			mypage.setSTag3(crdiProfileReq.getTag3());
			if (uuid != null) {
				mypage.setProfileImgUrl(imageService.requestImageUrl(uuid));
			}
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
	public void updateProfileImg(String email, String uuid) {
		boolean verify = verifyClientRequest(email);
		if (!verify) throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);

		try {
			String url = imageService.requestImageUrl(uuid);
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

	private boolean verifyClientRequest(String email) {
		if (email == null || !memberRepository.existsByEmail(email)) {
			return false;
		}
		return true;
	}
}

