package com.piesat.schedule.client.handler.backup;

import com.github.pagehelper.PageHelper;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.backup.BackupLogService;
import com.piesat.schedule.client.util.ExtractMessage;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.util.Md5Utils;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.vo.BackupVo;
import com.piesat.schedule.client.vo.ReplaceVo;
import com.piesat.schedule.client.vo.StrategyVo;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.mapper.backup.BackupLogMapper;
import com.piesat.util.ResultT;
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
    public void execute(JobInfoEntity jobInfoEntity) {
        BackupEntity backupEntity = (BackupEntity) jobInfoEntity;
        ResultT<String> resultT = new ResultT<>();
        this.preParam(backupEntity, resultT);
        log.info("备份调用成功");
    }

    public void preParam(BackupEntity backupEntity, ResultT<String> resultT) {
        /*****========1.查询备份最大日期============***/
        BackupLogEntity backupLogEntity = this.findMaxBackupTime(backupEntity.getId(), resultT);
        if (!resultT.isSuccess()) {
            return;
        }
        BackupLogEntity backupLogNewEntity = new BackupLogEntity();
        BeanUtils.copyProperties(backupEntity, backupLogNewEntity);
        /*****========2.计算备份时次============***/
        BackupVo backupVo = this.calculateBackupTime(backupEntity, backupEntity.getTriggerLastTime(), resultT);
        if (!resultT.isSuccess()) {
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

        for (BackupLogEntity log : compensateList) {
            log.setId(null);
            log.setJobId(backupEntity.getId());
            this.backupExecute(log, backupEntity, resultT);
            if(!resultT.isSuccess()){
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
                    compensateList.add(backupLogEntity);
                }
            }
            long startTime = backupLogEntity.getBackupTime()+backupVo.getMistiming();
            //isEnd 2为补偿备份
            while (backupVo.getBackupTime() - startTime >0) {
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
                if(backupHisVo.getBackupTime()>backupLogEntity.getBackupTime()){
                    compensateList.add(backupLogHisEntity);
                }
            }

        }
    }


    public BackupLogEntity insertBackupLog(BackupLogEntity backupLogEntity, BackupEntity backupEntity, ResultT<String> resultT) {
        ReplaceVo replaceVo = new ReplaceVo();
        replaceVo.setMsg(backupEntity.getStorageDirectory() + "/{databaseId}/{dataClassId}/{yyyy}/{yyyy-MM}");
        replaceVo.setDatabaseId(backupEntity.getParentId());
        replaceVo.setDataClassId(backupEntity.getDataClassId());
        replaceVo.setBackupTime(backupLogEntity.getBackupTime());
        ExtractMessage.getIndexOf(replaceVo, resultT);
        backupLogEntity.setId(null);
        backupLogEntity.setJobId(backupEntity.getId());
        backupLogEntity.setHandleCode("0");
        backupLogEntity.setTriggerCode(1);
        backupLogEntity.setStorageDirectory(replaceVo.getMsg());
        backupLogEntity.setHandleTime(new Date());
        backupLogEntity = backupLogService.saveNotNull(backupLogEntity);
        return backupLogEntity;
    }

    public void backupExecute(BackupLogEntity backupLog, BackupEntity backupEntity, ResultT<String> resultT) {
        StrategyVo strategyVo = new StrategyVo();
        strategyVo.setStartTime(System.currentTimeMillis());
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
            this.backUpTable(backupLogEntity,backupEntity,strategyVo,resultT);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            FileUtil.delFile(new File(strategyVo.getTempPtah()));
            FileUtil.delFile(new File(strategyVo.getTempZipPath()));
            FileUtil.delFileList(strategyVo.getDeleteFileList());
            strategyVo.setEndTime(System.currentTimeMillis());
            this.updateBackLog(strategyVo,backupLogEntity,resultT);
        }



    }
    public void checkBackupLogEntity(BackupLogEntity backupLogEntity,ResultT<String> resultT){
        if(!StringUtils.isNotNullString(backupLogEntity.getParentId())){
            resultT.setErrorMessage("物理库基础库ID不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(backupLogEntity.getDataClassId())){
            resultT.setErrorMessage("存储编码不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(backupLogEntity.getDdataId())){
            resultT.setErrorMessage("四级编码不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(backupLogEntity.getTableName())){
            resultT.setErrorMessage("表名不能为空");
            return;
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
        FileUtil.mkdirs(strategyVo.getTempPtah(), resultT);
        FileUtil.createFile(strategyVo.getTempPtah(), resultT);
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
        if (null != backupLogEntity.getVTableName()) {
            String vfileName = backupLogEntity.getParentId() + "--" + backupLogEntity.getVTableName() + "--" + backupTime + "--" + backupLogEntity.getDataClassId();
            strategyVo.setVfileName(vfileName);
            baseBusiness.backUpVtable(backupLogEntity, strategyVo, resultT);
        }
        if(!resultT.isSuccess()){
            return;
        }
        strategyVo.setTempZipPath(strategyVo.getTempPtah() + ".zip");
        /*****========5.压缩备份文件  ============***/
        ZipUtils.doCompress(strategyVo.getTempPtah(), strategyVo.getTempZipPath(), resultT);
        if(!resultT.isSuccess()){
            return;
        }
        this.calculateFileName(strategyVo,backupLogEntity,resultT);
        try {
            FileUtils.copyFile(new File(strategyVo.getTempZipPath()), new File(backupLogEntity.getStorageDirectory()+"/"+strategyVo.getRealFileName()));
        } catch (IOException e) {
            resultT.setErrorMessage("移动文件失败");
            e.printStackTrace();
        }
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
                backupTime = deafulBackupTime;
            }
            map.put("mistiming", mistiming);
            map.put(BACKUP_TIME, backupTime);
        } catch (Exception e) {
            resultT.setErrorMessage("计算历史时次出错");
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
            if(files.length>0){
                for(File f:files){
                    System.out.println(f.getName());
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
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void updateBackLog(StrategyVo strategyVo,BackupLogEntity backupLogEntity,ResultT<String> resultT){
        backupLogEntity.setFileName(strategyVo.getRealFileName());
        backupLogEntity.setHandleCode("1");
        backupLogEntity.setElapsedTime((strategyVo.getEndTime()-strategyVo.getStartTime())/1000);
        if(!resultT.isSuccess()){
            backupLogEntity.setHandleCode("2");
        }
        backupLogService.saveNotNull(backupLogEntity);
    }

    public BackupLogEntity findMaxBackupTime(String jobId, ResultT<String> resultT) {
        try {
            PageHelper.startPage(1, 1);
            List<BackupLogEntity> backupLogEntityList = backupLogMapper.findMaxBackupTime(jobId);
            if (backupLogEntityList != null && !backupLogEntityList.isEmpty()) {
                return backupLogEntityList.get(0);
            }
        } catch (Exception e) {
            resultT.setErrorMessage("获取最大时次出错");
            log.error(OwnException.get(e));
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
            resultT.setErrorMessage("获取日志出错");
            log.error(OwnException.get(e));

        }
        return null;

    }


}
