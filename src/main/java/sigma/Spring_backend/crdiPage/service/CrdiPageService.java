package sigma.Spring_backend.crdiPage.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sigma.Spring_backend.awsUtil.service.AwsService;
import sigma.Spring_backend.baseUtil.advice.BussinessExceptionMessage;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.crdiPage.dto.CrdiProfileReq;
import sigma.Spring_backend.crdiPage.entity.CrdiMyPage;
import sigma.Spring_backend.crdiPage.repository.CrdiPageRepository;
import sigma.Spring_backend.memberMypage.dto.MemberProfileImgReq;
import sigma.Spring_backend.memberMypage.entity.MemberMypage;
import sigma.Spring_backend.memberUtil.entity.Member;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrdiPageService {

    private final CrdiPageRepository crdiPageRepository;
    private final MemberRepository memberRepository;
    private final AwsService awsService;

    @Transactional
    public void registCrdiMypage(CrdiProfileReq crdiProfileReq) {

        if (!memberRepository.existsByEmail(crdiProfileReq.getEmail())) {
            throw new BussinessException(BussinessExceptionMessage.MEMBER_ERROR_NOT_FOUND);
        }

        boolean verify = verifyRequest(crdiProfileReq);
        if (!verify) throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_IMG_FORMAT);

        if (crdiProfileReq.getIntro().length() > 500) {
            throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_INTRO_LENGTH);
        }

        try {
            String url = awsService.imageUploadToS3("/profileImage", crdiProfileReq.getProfileImg());
            crdiPageRepository.save(CrdiMyPage.builder()
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
            throw new BussinessException(BussinessExceptionMessage.MEMBER_MYPAGE_ERROR_DB);
        }
    }

    private boolean verifyRequest(CrdiProfileReq request) {
        MultipartFile imageFile = request.getProfileImg();
        if (imageFile.isEmpty() || imageFile.getSize() == 0) {
            return false;
        }else{
            return true;
        }
    }
}
