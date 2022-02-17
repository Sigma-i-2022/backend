package sigma.Spring_backend.memberMypage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.baseUtil.advice.BussinessExceptionMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberMypage.repository.MemberMypageRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberMypageService {

	private final MemberMypageRepository memberMypageRepository;
	private final MemberRepository memberRepository;

	@Transactional(readOnly = true)
	public MemberMypage getMemberProfile(Map<String, String> memberProfileInfoMap) {
		String userEmail = memberProfileInfoMap.get("email");

		Optional<Member> optionalMember = memberRepository.findMemberByEmailUsingFetchJoin(userEmail);
		if (optionalMember.isPresent()) {
			return optionalMember.get().getMypage();
		} else {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
		}
	}

	@Transactional
	public void registMemberMypage(Map<String, String> memberMypage) {
		String userEmail = memberMypage.get("email");
		String userId = memberMypage.get("userId");
		String userIntro = "";
		if (memberMypage.containsKey("intro")) {
			userIntro = memberMypage.get("intro");
		}

		boolean verifyUserInfo = verifyUserInfo(memberMypage);
		if (!verifyUserInfo) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_NOT_FOUND);
		}

		boolean verifyMypage = verifyMypage(userEmail);
		if (!verifyMypage) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_EXIST);
		}

		if (userIntro.length() > 500) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_INTRO_LENGTH);
		}

		saveMemberMypage(MemberMypage.builder()
				.email(userEmail)
				.userId(userId)
				.introduction(userIntro)
				.build());
	}

	@Transactional
	public void updateMemberProfile(Map<String, String> memberProfileInfoMap) {
		String userEmail = memberProfileInfoMap.get("email");
		String userId = memberProfileInfoMap.get("userId");
		String userIntro = memberProfileInfoMap.get("intro");

		if (!memberMypageRepository.existsByEmail(userEmail)) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
		}

		MemberMypage originMypage = memberMypageRepository.findByEmail(userEmail).get();

		if (!originMypage.getUserId().equals(userId)) {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_NOT_FOUND);
		} else if (!originMypage.getIntroduction().equals(userIntro)) {
			try {
				originMypage.setIntroduction(userIntro);
				saveMemberMypage(originMypage);
			} catch (Exception e) {
				throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_DB);
			}
		}
	}

	@Transactional
	public void deleteMemberMypage(Map<String, String> memberProfileInfoMap) {
		String userEmail = memberProfileInfoMap.get("email");
		String userId = memberProfileInfoMap.get("userId");

		try {
			MemberMypage originProfile = memberMypageRepository.findByEmail(userEmail).get();

			if (!originProfile.getUserId().equals(userId)) {
				throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_NOT_FOUND);
			}

			memberRepository.deleteById(originProfile.getSeq());
		} catch (Exception e){
			throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
		}
	}

	private void saveMemberMypage(MemberMypage memberMypage) {
		memberMypageRepository.save(memberMypage);
	}

	private boolean verifyMypage(String email) {
		return !memberMypageRepository.existsByEmail(email);
	}

	private boolean verifyUserInfo(Map<String, String> memberProfileInfoMap) {
		String userEmail = memberProfileInfoMap.get("email");
		String userId = memberProfileInfoMap.get("userId");

		if (memberRepository.existsByEmail(userEmail)) {
			Member member = memberRepository.findByEmail(userEmail).get();
			return member.getUserId().equals(userId);
		} else {
			throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_NOT_FOUND);
		}
	}
}
