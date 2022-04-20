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
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.crdiBlock.repository.CrdiBlockRepository;
import sigma.Spring_backend.crdiPage.dto.CrdiRes;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkReq;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkRes;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;
import sigma.Spring_backend.crdiPage.repository.CrdiWorkRepository;
import sigma.Spring_backend.imageUtil.service.ImageService;
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
	private final ImageService imageService;


	@Transactional
	public void registCrdiWork(CrdiWorkReq crdiWorkReq, String uuid) {

		if (!verifyWork(crdiWorkReq)) throw new BussinessException("작품에 필요한 정보가 없습니다.");

		String imagePathUrl = imageService.requestImageUrl(uuid);

		try {
			Member member = memberRepository.findByEmail(crdiWorkReq.getCrdiEmail())
					.orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
			member.addWork(crdiWorkReq.toEntity(imagePathUrl));
		} catch (Exception e) {
			throw new BussinessException("DB 작품 저장 실패");
		}
	}

	@Transactional
	public void updateCrdiWork(Long workSeq, CrdiWorkReq crdiWorkReq){
		if(workSeq == null) throw new BussinessException("키 입력이 잘못되었습니다.");
		CrdiWork crdiWork = crdiWorkRepository.findById(workSeq)
			.orElseThrow(() -> new BussinessException("해당 작품이 존재하지 않습니다."));

			crdiWork.setExplanation(crdiWorkReq.getExplanation());
			crdiWork.setTopInfo(crdiWorkReq.getTopInfo());
			crdiWork.setBottomInfo(crdiWorkReq.getBottomInfo());
			crdiWork.setShoeInfo(crdiWorkReq.getShoeInfo());
			crdiWork.setKeyword1(crdiWorkReq.getKeyword1());
			crdiWork.setKeyword2(crdiWorkReq.getKeyword2());
			crdiWork.setKeyword3(crdiWorkReq.getKeyword3());
			crdiWork.setUpdateDate(new DateConfig().getNowDate());

	}

    @Transactional
    public void updateCrdiWorkImg(Long workSeq, String uuid){
        if(workSeq == null) throw new BussinessException("키 입력이 잘못되었습니다.");
        CrdiWork crdiWork = crdiWorkRepository.findById(workSeq)
                .orElseThrow(() -> new BussinessException("해당 작품이 존재하지 않습니다."));

		String imagePathUrl = imageService.requestImageUrl(uuid);
		crdiWork.setImagePathUrl(imagePathUrl);
        crdiWork.setUpdateDate(new DateConfig().getNowDate());
        crdiWorkRepository.save(crdiWork);

    }

    @Transactional
	public void deleteCrdiWork(Long workSeq){
		CrdiWork crdiWork = crdiWorkRepository.findById(workSeq)
				.orElseThrow(() -> new BussinessException("해당 작품이 존재하지 않습니다."));
		crdiWork.getMember().removeWork(crdiWork);
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

	private boolean verifyWork(CrdiWorkReq crdiWorkReq) {
		if (crdiWorkReq.getCrdiEmail() == null || crdiWorkReq.getCrdiEmail().equals("")) {
			return false;
		}

		return true;
	}
}
