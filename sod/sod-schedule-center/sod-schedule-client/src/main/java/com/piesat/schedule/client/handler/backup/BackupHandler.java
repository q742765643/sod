package com.piesat.schedule.client.handler.backup;

import com.github.pagehelper.PageHelper;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.backup.BackupLogService;
import com.piesat.schedule.client.util.*;
import com.piesat.schedule.client.vo.*;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.mapper.backup.BackupLogMapper;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Value("${backup.temp-path}")
    private String backupTempPath;

    private static final String BACKUP_TIME="backupTime";

    @Override
    public void execute(JobInfoEntity jobInfoEntity,ResultT<String> resultT) {
        log.info("备份调用成功");
        BackupEntity backupEntity = (BackupEntity) jobInfoEntity;
        long occurTime = System.currentTimeMillis();

        DiSendVo diSendVo = new DiSendVo();
        diSendVo.setStartTimeS(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(backupEntity.getTriggerLastTime()));
        diSendVo.setSendPhys(backupEntity.getParentId());
        diSendVo.setTaskId(backupEntity.getId());
        diSendVo.setDataType(backupEntity.getDdataId());
        diSendVo.setTaskName(backupEntity.getProfileName());
        diSendVo.setStartTimeA(System.currentTimeMillis());
        Map mapDetail = new HashMap(10);
        mapDetail.put("COMPLETEDATA", "");
        mapDetail.put("DETAIL", "");
        diSendVo.setTaskDetail(mapDetail);

        try {
            this.preParam(backupEntity, resultT);
        } catch (Exception e) {
            resultT.setErrorMessage("执行备份失败{}",OwnException.get(e));
        } finally {
            diSendVo.setEndTimeA(System.currentTimeMillis());
            if (resultT.isSuccess()) {
                diSendVo.setTaskState("成功");
            } else {
                log.error("备份任务ID{},任务名{},错误{}",backupEntity.getId(),backupEntity.getProfileName(),resultT);
                diSendVo.setTaskState("失败");
                diSendVo.setTaskErrorReason(resultT.getMsg());
                diSendVo.setTaskErrorTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis())));
            }
            diSendVo.setRecordTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis())));
            DiSendUtil.send(backupEntity.getTriggerLastTime(), "备份任务", diSendVo);

            if(!resultT.isSuccess()){
                EiSendVo eiSendVo=new EiSendVo();
                EiSendUtil.send(eiSendVo,0,backupEntity.getProfileName(),occurTime,resultT);
            }
        }


    }

    public void preParam(BackupEntity backupEntity, ResultT<String> resultT) {
        /*****========1.查询备份最大日期============***/
        BackupLogEntity backupLogEntity = this.findMaxBackupTime(backupEntity.getId(), resultT);
        if (!resultT.isSuccess()) {
            log.error("查询备份最大备份时间失败,资料名称:{}",backupEntity.getProfileName());
            return;
        }
        BackupLogEntity backupLogNewEntity = new BackupLogEntity();
        BeanUtils.copyProperties(backupEntity, backupLogNewEntity);
        /*****========2.计算备份时次============***/
        BackupVo backupVo = this.calculateBackupTime(backupEntity, backupEntity.getTriggerLastTime(), resultT);
        if (!resultT.isSuccess()) {
            log.error("计算备份时次失败,资料名称{}",backupEntity.getProfileName());
            return;
        }
        backupLogNewEntity.setBackupTime(backupVo.getBackupTime());
        backupLogNewEntity.setConditions(backupVo.getConditions());
        backupLogNewEntity.setSecondConditions(backupVo.getSecondConditions());
        backupLogNewEntity.setTriggerTime(backupEntity.getTriggerLastTime());

        /*****========3.获取历史丢失时次============***/
        List<BackupLogEntity> compensateList = new ArrayList<>();

        this.addHisBackupLog(backupEntity,backupVo,backupLogEntity,compensateList,resultT);
        if(!resultT.isSuccess()){
            log.error("计算获取历史丢失时次失败,资料名称{}",backupEntity.getProfileName());
            return;
        }
        /*****========4 isEnd 1为近时备份  ============***/
        backupLogNewEntity.setIsEnd(1);


        /*****========5.如果为区间备份 isEnd 0为增加远时备份  ============***/
        if (backupVo.getMistiming() > 0) {
            BackupLogEntity backupLogHisEntity = new BackupLogEntity();
            BeanUtils.copyProperties(backupEntity, backupLogHisEntity);
            backupLogHisEntity.setConditions(backupVo.getSecondConditions());
            backupLogHisEntity.setBackupTime(backupVo.getBackupTimeHis());
            backupLogHisEntity.setIsEnd(0);
            compensateList.add(backupLogHisEntity);
        }
        compensateList.add(backupLogNewEntity);

        for (BackupLogEntity backUpLog : compensateList) {
            backUpLog.setId(null);
            backUpLog.setJobId(backupEntity.getId());
            this.backupExecute(backUpLog, backupEntity, resultT);
            if(!resultT.isSuccess()){
                log.error("执行表备份失败,资料名称{}",backupEntity.getProfileName());
                return;
            }
        }


    }

    public void addHisBackupLog(BackupEntity backupEntity,BackupVo backupVo,BackupLogEntity backupLogEntity
            ,List<BackupLogEntity> compensateList,ResultT<String> resultT) {
        if (backupLogEntity != null && backupVo.getMistiming() > 0) {
            if (!"1".equals(backupLogEntity.getHandleCode())) {
                backupLogService.delete(backupLogEntity.getId());
                backupLogEntity.setIsEnd(2);
                if (backupLogEntity.getBackupTime() < backupVo.getBackupTime()) {
                    backupLogEntity.setHandleMsg(null);
                    compensateList.add(backupLogEntity);
                }
            }
            this.calculateCompensateList(backupEntity,backupVo,backupLogEntity.getBackupTime(),compensateList,resultT);

        }
        if (backupLogEntity == null && backupVo.getMistiming() > 0&&null!=backupEntity.getBackupStartTime()) {
            this.calculateCompensateList(backupEntity,backupVo,backupEntity.getBackupStartTime().getTime(),compensateList,resultT);
        }
    }

    public void  calculateCompensateList(BackupEntity backupEntity,BackupVo backupVo,long backTime
            ,List<BackupLogEntity> compensateList,ResultT<String> resultT){
        long startTime = backTime+backupVo.getMistiming();
        //isEnd 2为补偿备份
        while (backupVo.getBackupTime()+backupVo.getMistiming() - startTime >0) {
            startTime = startTime + backupVo.getMistiming();

            BackupLogEntity backupLogHisEntity = new BackupLogEntity();
            BeanUtils.copyProperties(backupEntity, backupLogHisEntity);
            BackupVo backupHisVo = this.calculateBackupTime(backupEntity, startTime, resultT);
            backupLogHisEntity.setBackupTime(backupHisVo.getBackupTime());
            backupLogHisEntity.setConditions(backupHisVo.getConditions());
            backupLogHisEntity.setSecondConditions(backupHisVo.getSecondConditions());
            backupLogHisEntity.setIsEnd(2);
            if (backupHisVo.getBackupTime() >= backupVo.getBackupTime()) {
                break;
            }
            if(backupHisVo.getBackupTime()>backTime){
                compensateList.add(backupLogHisEntity);
            }
        }
    }

    public BackupLogEntity insertBackupLog(BackupLogEntity backupLogEntity, BackupEntity backupEntity, ResultT<String> resultT) {
        ReplaceVo replaceVo = new ReplaceVo();
        replaceVo.setMsg(backupEntity.getStorageDirectory() + "/{databaseId}/{dataClassId}/{yyyy}/{yyyy-MM}");
        replaceVo.setDatabaseId(backupEntity.getParentId());
        replaceVo.setDataClassId(backupEntity.getDataClassId());
        replaceVo.setDdataId(backupEntity.getDdataId());
        replaceVo.setBackupTime(backupLogEntity.getBackupTime());
        ExtractMessage.getIndexOf(replaceVo, resultT);
        if(!resultT.isSuccess()){
            return null;
        }
        backupLogEntity.setId(null);
        backupLogEntity.setJobId(backupEntity.getId());
        backupLogEntity.setHandleCode("0");
        backupLogEntity.setTriggerCode(1);
        backupLogEntity.setStorageDirectory(replaceVo.getMsg());
        backupLogEntity.setHandleTime(new Date());
        try {
            backupLogEntity = backupLogService.saveNotNull(backupLogEntity);
        } catch (Exception e) {
            resultT.setErrorMessage("插入日志失败:{}",OwnException.get(e));
            log.error("插入日志失败:{}",OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }
        return backupLogEntity;
    }

    public void backupExecute(BackupLogEntity backupLog, BackupEntity backupEntity, ResultT<String> resultT) {
        resultT.clearProcessMsg();
        StrategyVo strategyVo = new StrategyVo();
        strategyVo.setStartTime(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        resultT.setSuccessMessage("资料{}备份开始备份资料时间{}",backupEntity.getProfileName(),simpleDateFormat.format(new Date(backupLog.getBackupTime())));
        log.info("资料{}备份开始备份资料时间{}",backupEntity.getProfileName(),simpleDateFormat.format(new Date(backupLog.getBackupTime())));
        BackupLogEntity backupLogEntityHis = this.findByJobId(backupLog, resultT);
        if(!resultT.isSuccess()){
            return;
        }
        BackupLogEntity backupLogEntity=this.getBackupLog(backupLog,backupLogEntityHis,backupEntity,resultT);
        if(!resultT.isSuccess()){
            return;
        }
        if(backupLogEntity==null){
            return;
        }
        try {
            this.checkBackupLogEntity(backupLogEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            resultT.setSuccessMessage("参数校验成功");
            log.info("参数校验成功");
            this.backUpTable(backupLogEntity,backupEntity,strategyVo,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage("数据备份表过程失败,资料名称{},错误{}",backupEntity.getProfileName(),OwnException.get(e));
            log.error("数据备份表过程失败,资料名称{},错误{}",backupEntity.getProfileName(),OwnException.get(e));
        }finally {
            FileUtil.delFile(new File(strategyVo.getTempPtah()),resultT);
            FileUtil.delFile(new File(strategyVo.getTempZipPath()),resultT);
            FileUtil.delFileList(strategyVo.getDeleteFileList(),resultT);
            strategyVo.setEndTime(System.currentTimeMillis());
            this.updateBackLog(strategyVo,backupLogEntity,resultT);
        }
        resultT.setSuccessMessage("资料{}备份结束备份资料时间{}",backupEntity.getProfileName(),simpleDateFormat.format(new Date(backupLog.getBackupTime())));
        log.info("资料{}备份结束备份资料时间{}",backupEntity.getProfileName(),simpleDateFormat.format(new Date(backupLog.getBackupTime())));



    }
    public void checkBackupLogEntity(BackupLogEntity backupLogEntity,ResultT<String> resultT){
        if(!StringUtils.isNotNullString(backupLogEntity.getParentId())){
            resultT.setErrorMessage("物理库基础库ID不能为空");
            log.error("物理库基础库ID不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(backupLogEntity.getDataClassId())){
            resultT.setErrorMessage("存储编码不能为空");
            log.error("存储编码不能为空");

            return;
        }
        if(!StringUtils.isNotNullString(backupLogEntity.getDdataId())){
            resultT.setErrorMessage("四级编码不能为空");
            log.error("四级编码不能为空");

            return;
        }
        if(!StringUtils.isNotNullString(backupLogEntity.getTableName())){
            resultT.setErrorMessage("表名不能为空");
            log.error("表名不能为空");
        }

    }

    public void backUpTable(BackupLogEntity backupLogEntity,BackupEntity backupEntity,StrategyVo strategyVo,ResultT<String> resultT){
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        String backupTime = format.format(backupLogEntity.getBackupTime());
        String fileName = backupLogEntity.getParentId() + "--" + backupLogEntity.getTableName() + "--" + backupTime + "--" + backupLogEntity.getDataClassId();
        backupLogEntity.setFileName(fileName);
        /*****========3.获取枚举类  ============***/
        BusinessEnum businessEnum = BusinessEnum.match(backupEntity.getDatabaseType(), null);
        BaseBusiness baseBusiness = businessEnum.getBaseBusiness();
        strategyVo.setTempPtah(backupTempPath + "/" + fileName);
        strategyVo.setIndexPath(strategyVo.getTempPtah() + "/" + "index.sql");
        strategyVo.setTempZipPath(strategyVo.getTempPtah() + ".zip");
        resultT.setSuccessMessage("创建临时文件夹{}",strategyVo.getTempPtah());
        log.info("创建临时文件夹{}",strategyVo.getTempPtah());
        if(new File(strategyVo.getTempPtah()).exists()){
            FileUtil.delFile(new File(strategyVo.getTempPtah()),resultT);
        }
        FileUtil.mkdirs(strategyVo.getTempPtah(), resultT);
        //FileUtil.createFile(strategyVo.getTempPtah(), resultT);
        FileUtil.mkdirs(backupLogEntity.getStorageDirectory(),resultT);
        if(!resultT.isSuccess()){
            return;
        }
        /*****========4.先备份k表，再备份v表  ============***/
        if (null != backupLogEntity.getTableName()) {
            baseBusiness.backUpKtable(backupLogEntity, strategyVo, resultT);
        }
        if(!resultT.isSuccess()){
            return;
        }
        if (null != backupLogEntity.getVTableName()&&StringUtils.isNotNullString(backupLogEntity.getVTableName())){
            String vfileName = backupLogEntity.getParentId() + "--" + backupLogEntity.getVTableName() + "--" + backupTime + "--" + backupLogEntity.getDataClassId();
            strategyVo.setVfileName(vfileName);
            baseBusiness.backUpVtable(backupLogEntity, strategyVo, resultT);
        }
        if(!resultT.isSuccess()){
            return;
        }
        /*****========5.压缩备份文件  ============***/
        ZipUtils.doCompress(strategyVo.getTempPtah(), strategyVo.getTempZipPath(), resultT);
        if(!resultT.isSuccess()){
            return;
        }
        this.calculateFileName(strategyVo,backupLogEntity,resultT);
        FileUtil.copyFile(strategyVo.getTempZipPath(), backupLogEntity.getStorageDirectory()+"/"+strategyVo.getRealFileName(),resultT);
        FileUtil.delFile(new File(strategyVo.getTempPtah()),resultT);
        strategyVo.getDeleteFileList().remove(backupLogEntity.getStorageDirectory()+"/"+strategyVo.getRealFileName());

    }
    public BackupLogEntity getBackupLog(BackupLogEntity backupLog,BackupLogEntity backupLogEntityHis,
                                        BackupEntity backupEntity,ResultT<String> resultT){
        BackupLogEntity backupLogEntity=null;
        if (backupLog.getIsEnd() == 0) {
            /*****========1.查询远时备份日志  ============***/
            if(backupLogEntityHis == null){
                return backupLogEntity;
            }
            if(0 == backupLogEntityHis.getIsEnd() && "1".equals(backupLogEntityHis.getHandleCode())){
                return backupLogEntity;
            }

            backupLogEntityHis.setBackupTime(backupLog.getBackupTime());
            backupLogEntityHis.setConditions(backupLog.getConditions());
            backupLogEntityHis.setIsEnd(0);
            backupLogEntity = backupLogEntityHis;

        } else {
            /*****========2.插入日志正在运行中  ============***/

            if (backupLogEntityHis != null) {
                if ("1".equals(backupLogEntityHis.getHandleCode())) {
                    return backupLogEntity;
                }
                backupLogEntity = backupLogEntityHis;
            } else {
                backupLogEntity = this.insertBackupLog(backupLog, backupEntity, resultT);
            }

        }
        return backupLogEntity;
    }

    public BackupVo calculateBackupTime(BackupEntity backupEntity, long backupTime, ResultT<String> resultT) {
        BackupVo backupVo = new BackupVo();
        ReplaceVo replaceVo = new ReplaceVo();
        replaceVo.setMsg(backupEntity.getConditions());
        replaceVo.setDatabaseId(backupEntity.getParentId());
        replaceVo.setDataClassId(backupEntity.getDataClassId());
        replaceVo.setDdataId(backupEntity.getDdataId());
        replaceVo.setBackupTime(backupTime);
        ExtractMessage.getIndexOf(replaceVo, resultT);
        Map<String, Long> map = this.calculateMistiming(replaceVo.getTimeSet(), backupTime, resultT);
        backupVo.setBackupTime(map.get(BACKUP_TIME));
        backupVo.setMistiming(map.get("mistiming"));
        backupVo.setConditions(replaceVo.getMsg());
        replaceVo.setMsg(backupEntity.getSecondConditions());
        ExtractMessage.getIndexOf(replaceVo, resultT);
        if (backupVo.getMistiming() > 0) {
            Map<String, Long> mapHis = this.calculateMistiming(replaceVo.getTimeSet(), backupTime, resultT);
            backupVo.setBackupTimeHis(mapHis.get(BACKUP_TIME));
            backupVo.setSecondConditions(replaceVo.getMsg());
        }


        return backupVo;
    }

    public Map<String, Long> calculateMistiming(Set<Long> timeSet, long deafulBackupTime, ResultT<String> resultT) {
        Map<String, Long> map = null;
        try {
            map = new HashMap<>();
            long mistiming = 0;
            long backupTime = 0;
            if (timeSet.size() == 2) {
                List<Long> timeList = new ArrayList<>();
                for (long time : timeSet) {
                    timeList.add(time);
                }
                long time1 = timeList.get(0);
                long time2 = timeList.get(1);
                if (time1 < time2) {
                    backupTime = time1;
                } else {
                    backupTime = time2;
                }
                mistiming = Math.abs(time2 - time1);


            } else {
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date(deafulBackupTime));
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                backupTime = calendar.getTime().getTime();
            }
            map.put("mistiming", mistiming);
            map.put(BACKUP_TIME, backupTime);
        } catch (Exception e) {
            resultT.setErrorMessage("计算历史时次出错{}",OwnException.get(e));
            log.error("计算历史时次出错{}",OwnException.get(e));
            log.error(OwnException.get(e));

        }
        return map;

    }

    public void calculateFileName(StrategyVo strategyVo,BackupLogEntity backupLogEntity,ResultT<String> resultT) {
        String dateEl="(\\d{8})";
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMdd");
        String fileNameEl = backupLogEntity.getParentId() + "--" + backupLogEntity.getTableName() + "--"+dateEl+"--" + backupLogEntity.getDataClassId()+"--\\w[a-z0-9]*.zip.[1-9]\\d*";
        try {
            File file=new File(backupLogEntity.getStorageDirectory());
            File[] files=file.listFiles();
            Pattern pattern = Pattern.compile(fileNameEl);
            int sort=1;
            if(null!=files&&files.length>0){
                for(File f:files){
                    Matcher m = pattern.matcher(f.getName());
                    String str = "";
                    if (m.find()) {
                        str = m.group(1);
                    }
                    if(!"".equals(str)){
                        long time=format.parse(str).getTime();
                        if(time<backupLogEntity.getBackupTime()){
                            sort++;
                        }
                        if(time==backupLogEntity.getBackupTime()){
                            strategyVo.getDeleteFileList().add(f.getPath());
                        }

                    }

                }
            }
            String md5= Md5Utils.getFileMD5String(strategyVo.getTempZipPath(),resultT);
            String realFileName = backupLogEntity.getParentId() + "--" + backupLogEntity.getTableName() + "--"+format.format(backupLogEntity.getBackupTime())+"--" + backupLogEntity.getDataClassId()+"--"+md5+".zip"+"."+sort;
            strategyVo.setRealFileName(realFileName);
            resultT.setSuccessMessage("计算文件名成功,文件名为{}",realFileName);
            log.info("计算文件名成功,文件名为{}",realFileName);
        } catch (ParseException e) {
            resultT.setErrorMessage("正则匹配获取序号出错:{}",OwnException.get(e));
            log.error("正则匹配获取序号出错:{}",OwnException.get(e));

        }


    }

    public void updateBackLog(StrategyVo strategyVo,BackupLogEntity backupLogEntity,ResultT<String> resultT){
        try {
            backupLogEntity.setFileName(strategyVo.getRealFileName());
            backupLogEntity.setHandleCode("1");
            backupLogEntity.setElapsedTime((strategyVo.getEndTime()-strategyVo.getStartTime())/1000);
            if(!resultT.isSuccess()){
                backupLogEntity.setHandleCode("2");
            }
            backupLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            backupLogService.saveNotNull(backupLogEntity);
        } catch (Exception e){
            resultT.setErrorMessage("修改日志出错{}",OwnException.get(e));
            log.error("修改日志出错{}",OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }

    }

    public BackupLogEntity findMaxBackupTime(String jobId, ResultT<String> resultT) {
        try {
            PageHelper.startPage(1, 1);
            List<BackupLogEntity> backupLogEntityList = backupLogMapper.findMaxBackupTime(jobId);
            if (backupLogEntityList != null && !backupLogEntityList.isEmpty()) {
                return backupLogEntityList.get(0);
            }
        } catch (Exception e) {
            resultT.setErrorMessage("获取最大时次出错{}",OwnException.get(e));
            log.error("获取最大时次出错{}",OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }
        return null;

    }

    public BackupLogEntity findByJobId(BackupLogEntity backupLogEntity, ResultT<String> resultT) {
        try {
            PageHelper.startPage(1, 1);
            List<BackupLogEntity> backupLogEntityList = backupLogMapper.findByJobId(backupLogEntity);
            if (backupLogEntityList != null && !backupLogEntityList.isEmpty()) {
                return backupLogEntityList.get(0);
            }
        } catch (Exception e) {
            resultT.setErrorMessage("获取日志出错{}",OwnException.get(e));
            log.error("获取日志出错{}",OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }
        return null;

    }


}
