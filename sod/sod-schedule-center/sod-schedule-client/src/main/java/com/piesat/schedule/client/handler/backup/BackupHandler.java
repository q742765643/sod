package com.piesat.schedule.client.handler.backup;

import com.piesat.schedule.client.business.XuguBusiness;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.JobInfoLogService;
import com.piesat.schedule.client.util.ExtractMessage;
import com.piesat.schedule.client.vo.BackupVo;
import com.piesat.schedule.client.vo.ReplaceVo;
import com.piesat.schedule.constant.HandleConstant;
import com.piesat.schedule.dao.JobInfoLogDao;
import com.piesat.schedule.mapper.test.TestMapper;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.mapper.backup.BackupLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:17
 */
@Slf4j
@Service("backupHandler")
public class BackupHandler implements BaseHandler {
    @Autowired
    private BackupLogMapper backupLogMapper;
    @Autowired
    private TestMapper testMapper;
    @Autowired
    private JobInfoLogService jobInfoLogService;
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        BackupEntity backupEntity= (BackupEntity) jobInfoEntity;
        this.preParam(backupEntity);
        log.info("备份调用成功");
    }
    public void preParam(BackupEntity backupEntity){

        BackupLogEntity backupLogEntity=backupLogMapper.findMaxBackupTime(backupEntity.getId());
        DateFormat format= new SimpleDateFormat("yyyyMMddHHmm");
        BackupLogEntity backupLogNewEntity=new BackupLogEntity();
        BeanUtils.copyProperties(backupEntity,backupLogNewEntity);
        BackupVo backupVo=this.calculateBackupTime(backupEntity,backupEntity.getTriggerLastTime());
        backupLogNewEntity.setBackupTime(new Date(backupVo.getBackupTime()));
        backupLogNewEntity.setConditions(backupVo.getConditions());
        backupLogNewEntity.setSecondConditions(backupVo.getSecondConditions());

        List<BackupLogEntity> compensateList=new ArrayList<>();

        if(backupLogEntity!=null&&backupVo.getMistiming()>0){
            if(!"1".equals(backupLogEntity.getHandleCode())){
                jobInfoLogService.delete(backupLogEntity.getId());
                compensateList.add(backupLogEntity);
            }
            long startTime=backupLogEntity.getBackupTime().getTime();

            while (backupVo.getBackupTime()-startTime>backupVo.getMistiming()){
                startTime=startTime+backupVo.getMistiming();
                if(startTime>=backupVo.getBackupTime()){
                    break;
                }
                BackupLogEntity backupLogHisEntity=new BackupLogEntity();
                BeanUtils.copyProperties(backupEntity,backupLogEntity);
                BackupVo backupHisVo=this.calculateBackupTime(backupEntity,startTime);
                backupLogHisEntity.setBackupTime(new Date(backupHisVo.getBackupTime()));
                backupLogHisEntity.setConditions(backupHisVo.getConditions());
                backupLogHisEntity.setSecondConditions(backupHisVo.getSecondConditions());
                backupLogHisEntity.setIsEnd(0);
                compensateList.add(backupLogHisEntity);
            }


        }
        if(backupVo.getMistiming()==0){
            backupLogNewEntity.setIsEnd(1);
        }
        if(backupVo.getMistiming()>0){
            BackupLogEntity backupLogHisEntity=new BackupLogEntity();
            backupLogHisEntity.setConditions(backupVo.getSecondConditions());
            backupLogHisEntity.setBackupTime(new Date(backupVo.getBackupTimeHis()));
            backupLogHisEntity.setIsEnd(1);
            BeanUtils.copyProperties(backupEntity,backupLogHisEntity);
            compensateList.add(backupLogHisEntity);
        }
        compensateList.add(backupLogNewEntity);






    }

    public BackupLogEntity insertBackupLog( BackupLogEntity backupLogEntity,BackupEntity backupEntity){
        ReplaceVo replaceVo=new ReplaceVo();
        replaceVo.setMsg(backupEntity.getStorageDirectory()+"/{databaseId}/{yyyy}/{yyyy-MM}");
        replaceVo.setDatabaseId(backupEntity.getParentId());
        replaceVo.setDataClassId(backupEntity.getDataClassId());
        replaceVo.setBackupTime(backupLogEntity.getBackupTime().getTime());
        ExtractMessage.getIndexOf(replaceVo);
        backupLogEntity.setId(null);
        backupLogEntity.setJobId(backupEntity.getId());
        backupLogEntity.setHandleCode("0");
        backupLogEntity.setTriggerCode(1);
        backupLogEntity.setStorageDirectory(replaceVo.getMsg());
        backupLogEntity.setHandleTime(new Date());
        backupLogEntity= (BackupLogEntity) jobInfoLogService.saveNotNull(backupLogEntity);
        return backupLogEntity;
    }

    public void backupExecute(BackupLogEntity backupLogEntity,BackupEntity backupEntity){
        backupLogEntity=this.insertBackupLog(backupLogEntity,backupEntity);
        DateFormat format= new SimpleDateFormat("yyyyMMdd");
        String backupTime=format.format(backupLogEntity.getBackupTime());
        String fileName=backupLogEntity.getParentId()+ "--" + backupLogEntity.getTableName()+ "--" + backupTime+"--"+backupLogEntity.getDataClassId();
        backupLogEntity.setFileName(fileName);
        if(backupLogEntity.getIsEnd()==2){

        }
        XuguBusiness xuguBusiness=new XuguBusiness();
        if(null!=backupLogEntity.getTableName()){
            xuguBusiness.backUpKtable(backupLogEntity,backupLogEntity.getConditions());
        }
        if(null!=backupLogEntity.getVTableName()){
            xuguBusiness.backUpVtable(backupLogEntity,backupLogEntity.getConditions());
        }




    }

    public BackupVo calculateBackupTime(BackupEntity backupEntity,long backupTime){
        ReplaceVo replaceVo=new ReplaceVo();
        replaceVo.setMsg(backupEntity.getConditions());
        replaceVo.setDatabaseId(backupEntity.getParentId());
        replaceVo.setDataClassId(backupEntity.getDataClassId());
        replaceVo.setBackupTime(backupTime);
        ExtractMessage.getIndexOf(replaceVo);
        Map<String,Long> map=this.calculateMistiming(replaceVo.getTimeSet(),backupTime);
        BackupVo backupVo=new BackupVo();
        backupVo.setBackupTime(map.get("backupTime"));
        backupVo.setMistiming(map.get("mistiming"));
        backupVo.setConditions(replaceVo.getMsg());
        replaceVo.setMsg(backupEntity.getSecondConditions());
        ExtractMessage.getIndexOf(replaceVo);
        if(backupVo.getMistiming()>0){
            Map<String,Long> mapHis=this.calculateMistiming(replaceVo.getTimeSet(),backupTime);
            backupVo.setBackupTimeHis(mapHis.get("backupTime"));
            backupVo.setSecondConditions(replaceVo.getMsg());
        }


        return backupVo;
    }

    public Map<String,Long>  calculateMistiming(Set<String> timeSet,long deafulBackupTime){
        Map<String,Long> map=new HashMap<>();
        DateFormat format= new SimpleDateFormat("yyyyMMddHHmm");
        long mistiming=0;
        long backupTime=0;
        if(timeSet.size()==2){
            List<String> timeList=new ArrayList<>();
            for(String time:timeSet){
                timeList.add(time);
            }
            try {
                long time1=format.parse(timeList.get(0)).getTime();
                long time2=format.parse(timeList.get(1)).getTime();
                if(time1<time2){
                    backupTime=time1;
                }else{
                    backupTime=time2;
                }
                mistiming=Math.abs(time2-time1);
            } catch (ParseException e) {
                e.printStackTrace();
            }


        }else{
            backupTime=deafulBackupTime;
        }
        map.put("mistiming",mistiming);
        map.put("backupTime",backupTime);
        return map;

    }

}
