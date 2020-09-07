package com.piesat.schedule.rpc.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.rpc.vo.DiTaskConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: DataStoragePlatform
 * @描述
 * @创建人 zzj
 * @创建时间 2019/4/11 11:09
 */
@Slf4j
@Service
public class DiSendService{
    @Value("${DITASKADDURL}")
    private String ditaskaddurl;
    @Value("${DITASKDELURL}")
    private String ditaskdelurl;

    public void sendBackup(BackupEntity backupEntity){
        DiTaskConfiguration diTaskConfiguration=new DiTaskConfiguration();
        diTaskConfiguration.setTaskId(backupEntity.getId());
        diTaskConfiguration.setSystem("SOD");
        diTaskConfiguration.setTaskName(backupEntity.getProfileName());
        diTaskConfiguration.setTaskDuty("2");
        diTaskConfiguration.setTaskCron(backupEntity.getJobCron());
        diTaskConfiguration.setOvertime(backupEntity.getExecutorTimeout());
        diTaskConfiguration.setTaskMaxTime(backupEntity.getExecutorFailRetryCount());
        diTaskConfiguration.setOffset(1);
        diTaskConfiguration.setAlarmtime(30);
        try {
            diTaskConfiguration.setIsAlarm(Integer.parseInt(backupEntity.getIsAlarm()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        diTaskConfiguration.setDataType(backupEntity.getDdataId());
        diTaskConfiguration.setHandleProcess(0);
        diTaskConfiguration.setSrc(0);
        diTaskConfiguration.setSendPhys(backupEntity.getParentId());
        this.sendAddDi(JSON.toJSONString(diTaskConfiguration, SerializerFeature.WriteNullStringAsEmpty));

    }
    public void sendClear(ClearEntity clearEntity){
        DiTaskConfiguration diTaskConfiguration=new DiTaskConfiguration();
        diTaskConfiguration.setTaskId(clearEntity.getId());
        diTaskConfiguration.setSystem("SOD");
        diTaskConfiguration.setTaskName(clearEntity.getProfileName());
        diTaskConfiguration.setTaskDuty("1");
        diTaskConfiguration.setTaskCron(clearEntity.getJobCron());
        diTaskConfiguration.setOvertime(clearEntity.getExecutorTimeout());
        diTaskConfiguration.setTaskMaxTime(1);
        diTaskConfiguration.setOffset(1);
        diTaskConfiguration.setAlarmtime(30);

        diTaskConfiguration.setIsAlarm(Integer.parseInt(clearEntity.getIsAlarm()));

        diTaskConfiguration.setDataType(clearEntity.getDdataId());
        diTaskConfiguration.setHandleProcess(0);
        diTaskConfiguration.setSrc(0);
        diTaskConfiguration.setSendPhys(clearEntity.getParentId());
        if(clearEntity.getParentId().toUpperCase().indexOf("RADB")<=0){
            this.sendAddDi(JSON.toJSONString(diTaskConfiguration, SerializerFeature.WriteNullStringAsEmpty));
        }
    }
    public void sendMove(MoveEntity moveEntity){
        DiTaskConfiguration diTaskConfiguration=new DiTaskConfiguration();
        diTaskConfiguration.setTaskId(moveEntity.getId());
        diTaskConfiguration.setSystem("SOD");
        diTaskConfiguration.setTaskName(moveEntity.getProfileName());
        diTaskConfiguration.setTaskDuty("3");
        diTaskConfiguration.setTaskCron(moveEntity.getJobCron());
        diTaskConfiguration.setOvertime(moveEntity.getExecutorTimeout());
        diTaskConfiguration.setTaskMaxTime(1);
        diTaskConfiguration.setOffset(1);
        diTaskConfiguration.setAlarmtime(30);

        diTaskConfiguration.setIsAlarm(Integer.parseInt(moveEntity.getIsAlarm()));

        diTaskConfiguration.setDataType(moveEntity.getDdataId());
        diTaskConfiguration.setHandleProcess(0);
        diTaskConfiguration.setSrc(0);
        diTaskConfiguration.setSendPhys(moveEntity.getParentId());
        this.sendAddDi(JSON.toJSONString(diTaskConfiguration, SerializerFeature.WriteNullStringAsEmpty));
    }
    public void sendAddDi(String messge){
        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(2*1000);
            requestFactory.setReadTimeout(2*1000);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> httpEntity = new HttpEntity<>(messge, headers);
            RestTemplate rst = new RestTemplate(requestFactory);
            ResponseEntity<String> stringResponseEntity = rst.postForEntity(ditaskaddurl, httpEntity, String.class);
            log.info("di返回送信息:"+JSON.toJSONString(stringResponseEntity));
        } catch (Exception e) {
            log.error("di发送信息异常:"+messge);
            e.printStackTrace();
        }
    }
    public void sendDeleteDi(String taskId){
        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            requestFactory.setConnectTimeout(2*1000);
            requestFactory.setReadTimeout(2*1000);
            Map map=new HashMap();
            map.put("taskId",taskId);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/json; charset=UTF-8"));
            headers.add("Accept", MediaType.APPLICATION_JSON.toString());
            HttpEntity<String> httpEntity = new HttpEntity<>(JSON.toJSONString(map), headers);
            RestTemplate rst = new RestTemplate(requestFactory);
            ResponseEntity<String> stringResponseEntity = rst.postForEntity(ditaskdelurl, httpEntity, String.class);
            log.info("di发送返回信息:"+JSON.toJSONString(stringResponseEntity));
        } catch (Exception e) {
            log.error("di发送信息异常:"+taskId);

        }
    }
}
