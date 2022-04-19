package sigma.Spring_backend.crdiPage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.crdiBlock.repository.CrdiBlockRepository;
import sigma.Spring_backend.crdiPage.dto.CrdiRes;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkReq;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkRes;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;
import sigma.Spring_backend.crdiPage.repository.CrdiWorkRepository;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;
import sigma.Spring_backend.review.dto.ReviewRes;
import sigma.Spring_backend.review.entity.Review;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class CrdiPageService {

	private final CrdiWorkRepository crdiWorkRepository;
	private final CrdiBlockRepository crdiBlockRepository;
	private final MemberRepository memberRepository;
	private final AwsService awsService;

	@Transactional
	public void registCrdiWork(CrdiWorkReq crdiWorkReq, MultipartFile imageFile) {

		if (!verifyWork(crdiWorkReq,imageFile)) throw new BussinessException("작품에 필요한 정보가 없습니다.");

		String imagePathUrl = awsService.imageUploadToS3("/crdiWorkImage", imageFile);

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
	public List<CrdiWorkRes> getWorks(String crdiEmail, PageRequest pageRequest) {
		return crdiWorkRepository.findAllByEmail(crdiEmail,pageRequest)
				.stream()
				.map(CrdiWork::toDto)
				.collect(Collectors.toList());
	}

	@Transactional(readOnly = true)
	public List<CrdiRes> getCrdiList(String email) {
		String crdiYn = "Y";
		List<String> crdiBlockList = crdiBlockRepository.findByEmail(email);

		List<CrdiRes> crdiRes = new ArrayList<>();
		List<Member> memberList = new ArrayList<>();
		List<String> imageWorkList = new ArrayList<>();
		double reviewStart = 0;
		if (!crdiBlockList.isEmpty()){
			memberList = memberRepository.findByCrdiYnAndEmailNotIn(crdiYn, crdiBlockList);
		}else {
			memberList = memberRepository.findByCrdiYn(crdiYn);
		}
			for(Member member : memberList){
				CrdiRes crdi = new CrdiRes();

				imageWorkList = member.getWork().stream().map(CrdiWork::getImagePathUrl).collect(Collectors.toList());
				reviewStart = member.getReviews().stream().mapToDouble(Review::getStar).average().orElse(0);
				crdi.setImageWorkImageList(imageWorkList);
				crdi.setImagePathUrl(member.getMypage().getProfileImgUrl());
				crdi.setId(member.getUserId());
				crdi.setSTag1(member.getMypage().getSTag1());
				crdi.setSTag2(member.getMypage().getSTag2());
				crdi.setSTag3(member.getMypage().getSTag3());
				crdi.setStar((int)reviewStart);

				crdiRes.add(crdi);

			}

		return crdiRes;
	}

	private boolean verifyWork(CrdiWorkReq crdiWorkReq,MultipartFile imageFile) {
		if (crdiWorkReq.getCrdiEmail() == null || crdiWorkReq.getCrdiEmail().equals("")) {
			return false;
		}
		if (imageFile == null || imageFile.isEmpty()) {
			return false;
		}

		return true;
	}
}
