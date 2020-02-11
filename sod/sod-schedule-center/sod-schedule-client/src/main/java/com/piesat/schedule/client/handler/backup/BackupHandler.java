package com.piesat.schedule.client.handler.backup;

import com.github.pagehelper.PageHelper;
import com.piesat.schedule.client.business.XuguBusiness;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.JobInfoLogService;
import com.piesat.schedule.client.service.backup.BackupLogService;
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
    private BackupLogService backupLogService;
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        BackupEntity backupEntity= (BackupEntity) jobInfoEntity;
        this.preParam(backupEntity);
        log.info("备份调用成功");
    }
    public void preParam(BackupEntity backupEntity){
        /*****========1.查询备份最大日期============***/
        BackupLogEntity backupLogEntity=this.findMaxBackupTime(backupEntity.getId());
        DateFormat format= new SimpleDateFormat("yyyyMMddHHmm");
        BackupLogEntity backupLogNewEntity=new BackupLogEntity();
        BeanUtils.copyProperties(backupEntity,backupLogNewEntity);
        /*****========2.计算备份时次============***/
        BackupVo backupVo=this.calculateBackupTime(backupEntity,backupEntity.getTriggerLastTime());
        backupLogNewEntity.setBackupTime(backupVo.getBackupTime());
        backupLogNewEntity.setConditions(backupVo.getConditions());
        backupLogNewEntity.setSecondConditions(backupVo.getSecondConditions());
        backupLogNewEntity.setTriggerTime(backupEntity.getTriggerLastTime());

        /*****========3.获取历史丢失时次============***/
        List<BackupLogEntity> compensateList=new ArrayList<>();

        if(backupLogEntity!=null&&backupVo.getMistiming()>0){
            if(!"1".equals(backupLogEntity.getHandleCode())){
                backupLogService.delete(backupLogEntity.getId());
                backupLogEntity.setIsEnd(2);
                if(backupLogEntity.getBackupTime()<backupVo.getBackupTime()){
                    compensateList.add(backupLogEntity);
                }
            }
            long startTime=backupLogEntity.getBackupTime();
            //isEnd 2为补偿备份
            while (backupVo.getBackupTime()-startTime>backupVo.getMistiming()){
                startTime=startTime+backupVo.getMistiming();
                if(startTime>=backupVo.getBackupTime()){
                    break;
                }
                BackupLogEntity backupLogHisEntity=new BackupLogEntity();
                BeanUtils.copyProperties(backupEntity,backupLogEntity);
                BackupVo backupHisVo=this.calculateBackupTime(backupEntity,startTime);
                backupLogHisEntity.setBackupTime(backupHisVo.getBackupTime());
                backupLogHisEntity.setConditions(backupHisVo.getConditions());
                backupLogHisEntity.setSecondConditions(backupHisVo.getSecondConditions());
                backupLogHisEntity.setIsEnd(2);
                compensateList.add(backupLogHisEntity);
            }


        }
        /*****========4 isEnd 1为近时备份  ============***/
        backupLogNewEntity.setIsEnd(1);


        /*****========5.如果为区间备份 isEnd 0为增加远时备份  ============***/
        if(backupVo.getMistiming()>0){
            BackupLogEntity backupLogHisEntity=new BackupLogEntity();
            BeanUtils.copyProperties(backupEntity,backupLogHisEntity);
            backupLogHisEntity.setConditions(backupVo.getSecondConditions());
            backupLogHisEntity.setBackupTime(backupVo.getBackupTimeHis());
            backupLogHisEntity.setIsEnd(0);
            compensateList.add(backupLogHisEntity);
        }
        compensateList.add(backupLogNewEntity);

        for(BackupLogEntity log:compensateList){
            this.backupExecute(log,backupEntity);
        }



    }

    public BackupLogEntity insertBackupLog( BackupLogEntity backupLogEntity,BackupEntity backupEntity){
        ReplaceVo replaceVo=new ReplaceVo();
        replaceVo.setMsg(backupEntity.getStorageDirectory()+"/{databaseId}/{yyyy}/{yyyy-MM}");
        replaceVo.setDatabaseId(backupEntity.getParentId());
        replaceVo.setDataClassId(backupEntity.getDataClassId());
        replaceVo.setBackupTime(backupLogEntity.getBackupTime());
        ExtractMessage.getIndexOf(replaceVo);
        backupLogEntity.setId(null);
        backupLogEntity.setJobId(backupEntity.getId());
        backupLogEntity.setHandleCode("0");
        backupLogEntity.setTriggerCode(1);
        backupLogEntity.setStorageDirectory(replaceVo.getMsg());
        backupLogEntity.setHandleTime(new Date());
        backupLogEntity=backupLogService.saveNotNull(backupLogEntity);
        return backupLogEntity;
    }

    public void backupExecute(BackupLogEntity backupLog,BackupEntity backupEntity){
        BackupLogEntity backupLogEntity=null;
        if(backupLog.getIsEnd()==0){
            /*****========1.查询远时备份日志  ============***/
            BackupLogEntity backupLogEntityHis=this.findByJobId(backupLog);
            if(backupLogEntityHis==null){
                return;
            }else{
                if(0==backupLogEntityHis.getIsEnd()&&"1".equals(backupLogEntityHis.getHandleCode())){
                    return;
                }
            }
            backupLogEntityHis.setBackupTime(backupLog.getBackupTime());
            backupLogEntityHis.setConditions(backupLog.getConditions());
            backupLogEntity=backupLogEntityHis;

        }else{
            /*****========2.插入日志正在运行中  ============***/
            BackupLogEntity backupLogEntityNew=this.findByJobId(backupLog);
            if(backupLogEntityNew!=null){
                if("1".equals(backupLogEntityNew.getHandleCode())){
                    return;
                }else{
                    backupLogEntity=backupLogEntityNew;
                }
            }else{
                backupLogEntity=this.insertBackupLog(backupLog,backupEntity);
            }

        }

        DateFormat format= new SimpleDateFormat("yyyyMMdd");
        String backupTime=format.format(backupLogEntity.getBackupTime());
        String fileName=backupLogEntity.getParentId()+ "--" + backupLogEntity.getTableName()+ "--" + backupTime+"--"+backupLogEntity.getDataClassId();
        backupLogEntity.setFileName(fileName);
        Map<String,Object> map=new HashMap<>();
        XuguBusiness xuguBusiness=new XuguBusiness();
        if(null!=backupLogEntity.getTableName()){
            xuguBusiness.backUpKtable(backupLogEntity,map);
        }
        if(null!=backupLogEntity.getVTableName()){
            String vfileName=backupLogEntity.getParentId()+ "--" + backupLogEntity.getVTableName()+ "--" + backupTime+"--"+backupLogEntity.getDataClassId();
            map.put("vfileName",vfileName);
            xuguBusiness.backUpVtable(backupLogEntity,map);
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

    public BackupLogEntity findMaxBackupTime(String jobId){
        PageHelper.startPage(1,1);
        List<BackupLogEntity> backupLogEntityList=backupLogMapper.findMaxBackupTime(jobId);
        if(backupLogEntityList!=null&& backupLogEntityList.size()>0){
            return backupLogEntityList.get(0);
        }
        return null;

    }
    public BackupLogEntity findByJobId(BackupLogEntity backupLogEntity){
        PageHelper.startPage(1,1);
        List<BackupLogEntity> backupLogEntityList=backupLogMapper.findByJobId(backupLogEntity);
        if(backupLogEntityList!=null&& backupLogEntityList.size()>0){
            return backupLogEntityList.get(0);
        }
        return null;

    }

}
