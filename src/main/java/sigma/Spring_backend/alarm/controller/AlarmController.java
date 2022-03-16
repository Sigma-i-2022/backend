package sigma.Spring_backend.alarm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sigma.Spring_backend.alarm.service.AlarmService;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.service.ResponseService;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Api(tags = "10. 알람")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class AlarmController {

    private final AlarmService alarmService;
    private final ResponseService responseService;
    private final int FAIL = -1;

    @PostMapping("/alarm")
    @ApiOperation(value = "알람 저장", notes = "알림을 저장합니다.")
    public CommonResult alarmSave(
            @ApiParam(value = "이메일", required = true)
            @RequestParam String email,
            @ApiParam(value = "알림내용", required = true)
            @RequestParam String alarmMsg
    ){
        Map<String, String> alarmMap = new HashMap<>();
        alarmMap.put("email", email);
        alarmMap.put("alarmMsg", alarmMsg);
        try{
            alarmService.saveAlarm(alarmMap);
            return responseService.getSuccessResult();
        }catch (Exception e){
            log.error(e.getMessage());
            return responseService.getFailResult(
                    FAIL,
                    e.getMessage()
            );
        }
    }
}
