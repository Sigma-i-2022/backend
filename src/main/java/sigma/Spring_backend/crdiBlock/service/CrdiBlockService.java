package sigma.Spring_backend.crdiBlock.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.baseUtil.advice.ExMessage;
import sigma.Spring_backend.baseUtil.config.DateConfig;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.crdiBlock.entity.CrdiBlock;
import sigma.Spring_backend.crdiBlock.repository.CrdiBlockRepository;
import sigma.Spring_backend.memberUtil.repository.MemberRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrdiBlockService {

    private final CrdiBlockRepository crdiBlockRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public boolean blockCrdi(String email, String crdiEmail) {
             try{
                 CrdiBlock crdiBlock = new CrdiBlock();

                 crdiBlock.setEmail(email);
                 crdiBlock.setCrdiEmail(crdiEmail);
                 crdiBlock.setRegDt(new DateConfig().getNowDate());
                 log.info("dflajsflsj"+crdiBlock);
                 memberRepository.findByEmail(email).ifPresentOrElse(
                         M -> M.addBlock(crdiBlock)
                         , () -> {
                             throw new BussinessException(ExMessage.MEMBER_ERROR_NOT_FOUND);
                         }
                 );
             }catch (Exception e){
                 throw new BussinessException(ExMessage.MEMBER_ERROR_DB_SAVE);
             }
        return true;
    }
}
