package sigma.Spring_backend.alarm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sigma.Spring_backend.alarm.entity.Alarm;
import sigma.Spring_backend.alarm.repository.AlarmRepository;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
import sigma.Spring_backend.memberUtil.entity.Member;

import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AlarmService {

    private final AlarmRepository alarmRepository;

    @Transactional
    public void saveAlarm(Map<String, String> alarmMap){

        String email = alarmMap.get("email");
        String alarmMsg = alarmMap.get("alarmMsg");

        if(alarmMsg == null || alarmMsg.equals("")){
            throw new BussinessException("알람메세지를 입력해주세요.");
        }else{
            try{
                Member member = new Member();
                Alarm alarm = new Alarm();
                alarm.setMember(member);
               alarmRepository.save(Alarm.builder()
                       .email(email)
                       .alarmMsg(alarmMsg)
                       .build()).toDto();
            }catch (Exception e){
                throw new BussinessException("알람저장에 실패하였습니다.");
            }
        }
    }
}
