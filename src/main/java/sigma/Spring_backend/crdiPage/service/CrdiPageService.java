package sigma.Spring_backend.crdiPage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.crdiPage.dto.CrdiProfileReq;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkReq;
import sigma.Spring_backend.crdiPage.dto.CrdiWorkRes;
import sigma.Spring_backend.crdiPage.entity.CrdiMypage;
import sigma.Spring_backend.crdiPage.entity.CrdiWork;
import sigma.Spring_backend.crdiPage.repository.CrdiPageRepository;
import sigma.Spring_backend.crdiPage.repository.CrdiWorkRepository;
import sigma.Spring_backend.memberLook.dto.MemberLookPageRes;
import sigma.Spring_backend.memberLook.entity.MemberLookPage;
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

    private final CrdiPageRepository crdiPageRepository;
    private final CrdiWorkRepository crdiWorkRepository;
    private final MemberRepository memberRepository;
    private final AwsService awsService;

    @Transactional
    public void registCrdiMypage(CrdiProfileReq crdiProfileReq) {

        if (!memberRepository.existsByEmail(crdiProfileReq.getEmail())) {
            throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);
        }

        boolean verify = verifyRequest(crdiProfileReq);
        if (!verify) throw new BussinessException(ExMessage.MEMBER_MYPAGE_IMG_FORMAT);

        if (crdiProfileReq.getIntro().length() > 500) {
            throw new BussinessException(ExMessage.MEMBER_MYPAGE_ERROR_INTRO_LENGTH);
        }

        try {
            String url = awsService.imageUploadToS3("/profileImage", crdiProfileReq.getProfileImg());
            crdiPageRepository.save(CrdiMypage.builder()
                    .email(crdiProfileReq.getEmail())
                    .userId(crdiProfileReq.getUserId())
                    .introduction(crdiProfileReq.getIntro())
                    .expertYN(crdiProfileReq.getExpertYN())
                    .sTag1(crdiProfileReq.getSTag1())
                    .sTag2(crdiProfileReq.getSTag2())
                    .sTag3(crdiProfileReq.getSTag3())
                    .profileImgUrl(url)
                    .build());
        } catch (Exception e) {
            throw new BussinessException(ExMessage.MEMBER_MYPAGE_ERROR_DB);
        }
    }

    @Transactional
    public void updateCrdiMypage(CrdiProfileReq crdiProfileReq) {

        if (!memberRepository.existsByEmail(crdiProfileReq.getEmail())) {
            throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);
        }

        if (!crdiPageRepository.existsByEmail(crdiProfileReq.getEmail())) {
            throw new BussinessException(ExMessage.MEMBER_MYPAGE_ERROR_NOT_FOUND);
        }else{
            try{
                CrdiMypage myPage = crdiPageRepository.findByEmail(crdiProfileReq.getEmail());
                myPage.setIntroduction(crdiProfileReq.getIntro());
                crdiPageRepository.save(myPage);
            }catch (Exception e) {
                throw new BussinessException(ExMessage.MEMBER_MYPAGE_ERROR_DB);
            }
        }

    }

    @Transactional
    public void registCrdiWork(CrdiWorkReq crdiWorkReq){

        if(!verifyWork(crdiWorkReq)) throw new BussinessException("작품에 필요한 정보가 없습니다.");

        String imagePathUrl = awsService.imageUploadToS3("/crdiWorkImage", crdiWorkReq.getImageFile());

        try{
            Member member = memberRepository.findByEmail(crdiWorkReq.getCrdiEmail())
                    .orElseThrow(() -> new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND));
            member.addWork(crdiWorkReq.toEntity(imagePathUrl));
        }catch (Exception e){
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
        Optional<Member> memberOpt = memberRepository.findByCrdiEmailFJ(crdiEmail);

        return memberOpt.map(m -> m.getWork()
                        .stream()
                        .map(CrdiWork::toDto)
                        .collect(Collectors.toList()))
                .orElseGet(ArrayList::new);
    }

    private boolean verifyRequest(CrdiProfileReq request) {
        MultipartFile imageFile = request.getProfileImg();
        if (imageFile.isEmpty() || imageFile.getSize() == 0) {
            return false;
        }else{
            return true;
        }
    }

    private boolean verifyWork(CrdiWorkReq crdiWorkReq){
        if(crdiWorkReq.getCrdiEmail() == null || crdiWorkReq.getCrdiEmail().equals("")){
            return false;
        }
        if(crdiWorkReq.getImageFile() == null || crdiWorkReq.getImageFile().equals("")){
            return false;
        }

        return true;
    }
}
