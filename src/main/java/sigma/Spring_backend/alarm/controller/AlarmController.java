package sigma.Spring_backend.alarm.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sigma.Spring_backend.alarm.dto.AlarmResponseDto;
import sigma.Spring_backend.alarm.service.AlarmService;
import sigma.Spring_backend.baseUtil.dto.CommonResult;
import sigma.Spring_backend.baseUtil.dto.ListResult;
import sigma.Spring_backend.baseUtil.exception.BussinessException;
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

    @GetMapping
    @ApiOperation(value = "알림 리스트 조회")
    public ListResult<AlarmResponseDto> getAlarmList(
            @ApiParam(value = "고객 이메일") @RequestParam String email
    ){
        try{
            return responseService.getListResult(alarmService.getAlarmList(email));
        }catch(Exception e){
            e.printStackTrace();
            throw new BussinessException(e.getMessage());
        }
    }
}
