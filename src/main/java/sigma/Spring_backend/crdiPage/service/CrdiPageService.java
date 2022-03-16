package sigma.Spring_backend.crdiPage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkReq;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkRes;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;
import sigma.Spring_backend.crdiPage.repository.CrdiWorkRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CrdiPageService {

	private final CrdiWorkRepository crdiWorkRepository;
	private final MemberRepository memberRepository;
	private final AwsService awsService;

	@Transactional
	public void registCrdiWork(CrdiWorkReq crdiWorkReq) {

		if (!verifyWork(crdiWorkReq)) throw new BussinessException("작품에 필요한 정보가 없습니다.");

		String imagePathUrl = awsService.imageUploadToS3("/crdiWorkImage", crdiWorkReq.getImageFile());

		try {
			Member member = memberRepository.findByEmail(crdiWorkReq.getCrdiEmail())
					.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
			member.addWork(crdiWorkReq.toEntity(imagePathUrl));
		} catch (Exception e) {
			throw new BussinessException("DB 작품 저장 실패");
		}
	}

	@Transactional(readOnly = true)
	public CrdiWorkRes getWork(Long key) {

		CrdiWork crdiWork = crdiWorkRepository.findById(key)
				.orElseThrow(() -> new BussinessException("해당하는 작품이 없습니다."));
		return crdiWork.toDto();
	}

	@Transactional(readOnly = true)
	public List<CrdiWorkRes> getWorks(String crdiEmail) {
		Optional<Member> memberOpt = memberRepository.findByEmailFJ(crdiEmail);

		return memberOpt.map(m -> m.getWork()
						.stream()
						.map(CrdiWork::toDto)
						.collect(Collectors.toList()))
				.orElseGet(ArrayList::new);
	}

	private boolean verifyWork(CrdiWorkReq crdiWorkReq) {
		if (crdiWorkReq.getCrdiEmail() == null || crdiWorkReq.getCrdiEmail().equals("")) {
			return false;
		}
		if (crdiWorkReq.getImageFile() == null || crdiWorkReq.getImageFile().isEmpty()) {
			return false;
		}

		return true;
	}
}
