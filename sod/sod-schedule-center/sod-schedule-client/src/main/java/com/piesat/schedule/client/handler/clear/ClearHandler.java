package com.piesat.schedule.client.handler.clear;

import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.business.GbaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.DatabaseOperationService;
import com.piesat.schedule.client.service.clear.ClearLogService;
import com.piesat.schedule.client.util.DiSendUtil;
import com.piesat.schedule.client.util.EiSendUtil;
import com.piesat.schedule.client.util.ExtractMessage;
import com.piesat.schedule.client.vo.*;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:22
 */
@Slf4j
@Service("clearHandler")
public class ClearHandler implements BaseHandler {
    @Autowired
    private ClearLogService clearLogService;
    @Autowired
    private DatabaseOperationService databaseOperationService;
    @Override
    public void execute(JobInfoEntity jobInfoEntity,ResultT<String> resultT) {
        log.info("清除调用成功");
        ClearEntity clearEntity= (ClearEntity) jobInfoEntity;
        this.preParam(clearEntity,resultT);
    }

    public void preParam( ClearEntity clearEntity, ResultT<String> resultT) {
        DiSendVo diSendVo = new DiSendVo();
        ClearVo clearVo=new ClearVo();
        clearVo.setStartTime(System.currentTimeMillis());
        long occurTime = System.currentTimeMillis();
        diSendVo.setStartTimeS(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(clearEntity.getTriggerLastTime()));
        diSendVo.setSendPhys(clearEntity.getParentId());
        diSendVo.setTaskId(clearEntity.getId());
        diSendVo.setDataType(clearEntity.getDdataId());
        diSendVo.setTaskName(clearEntity.getProfileName());
        diSendVo.setStartTimeA(System.currentTimeMillis());
        try {

            this.clearExecute(clearVo,clearEntity,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage("清除异常:{}",OwnException.get(e));
            log.error("清除异常:{}",OwnException.get(e));
        }finally {
            Map mapDetail = new HashMap(10);
            mapDetail.put("COMPLETEDATA", clearVo.getCount());
            mapDetail.put("DETAIL", resultT.getProcessMsg());
            diSendVo.setTaskDetail(mapDetail);
            diSendVo.setEndTimeA(System.currentTimeMillis());
            if (resultT.isSuccess()) {
                diSendVo.setTaskState("成功");
            } else {
                log.error("清除任务ID{},任务名{},错误{}",clearEntity.getId(),clearEntity.getProfileName(),resultT.getMsg());
                diSendVo.setTaskState("失败");
                diSendVo.setTaskErrorReason(resultT.getMsg());
                diSendVo.setTaskErrorTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis())));
            }
            diSendVo.setRecordTime(new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis())));
            DiSendUtil.send(clearEntity.getTriggerLastTime(), "清除任务", diSendVo);

            if(!resultT.isSuccess()){
                EiSendVo eiSendVo=new EiSendVo();
                EiSendUtil.send(eiSendVo,2,clearEntity.getProfileName(),occurTime,resultT);
            }
        }
    }
    public void clearExecute(ClearVo clearVo,ClearEntity clearEntity, ResultT<String> resultT){
        ClearLogEntity clearLogEntity=new ClearLogEntity();
        try {
            BeanUtils.copyProperties(clearEntity,clearLogEntity);
            clearLogEntity.setId(null);
            clearLogEntity.setTriggerTime(clearEntity.getTriggerLastTime());

            this.calculateTime(clearVo,clearEntity,resultT);
            clearLogEntity.setConditions(clearVo.getConditions());
            clearLogEntity.setClearTime(clearVo.getClearTime());
            this.checkClearLogEntity(clearLogEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            resultT.setSuccessMessage("参数校验成功");
            log.info("参数校验成功");

            BusinessEnum businessEnum = BusinessEnum.match(clearEntity.getDatabaseType(), null);
            BaseBusiness baseBusiness = businessEnum.getBaseBusiness();
            if(!"XUGU".equals(clearEntity.getDatabaseType().toUpperCase())){
                long count=baseBusiness.selectTableCount(clearEntity.getParentId(),clearEntity.getTableName(),clearVo.getConditions(),resultT);
                if(!resultT.isSuccess()){
                    return;
                }
                clearVo.setCount(count);
                if(count==0){
                    resultT.setSuccessMessage("清除条数为0条");
                    log.info("清除条数为0条");
                    return;
                }
            }
            clearLogEntity=this.insertClearLog(clearEntity,clearLogEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            resultT.setSuccessMessage("插入日志成功");
            log.info("插入日志成功");

            baseBusiness.deleteKtable(clearLogEntity,clearVo,resultT);
            clearLogEntity.setClearCount(clearVo.getCount());
        } catch (BeansException e) {
            resultT.setErrorMessage("清除异常:{}",OwnException.get(e));
            log.error("清除异常:{}",OwnException.get(e));
        }finally {
            clearVo.setEndTime(System.currentTimeMillis());
            this.updateClearLog(clearVo,clearLogEntity,resultT);
        }

    }
    public void checkClearLogEntity(ClearLogEntity clearLogEntity, ResultT<String> resultT){
        if(!StringUtils.isNotNullString(clearLogEntity.getParentId())){
            resultT.setErrorMessage("物理库基础库ID不能为空");
            log.error("物理库基础库ID不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(clearLogEntity.getDataClassId())){
            resultT.setErrorMessage("存储编码不能为空");
            log.error("存储编码不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(clearLogEntity.getDdataId())){
            resultT.setErrorMessage("四级编码不能为空");
            log.error("四级编码不能为空");
            return;
        }
        if(!StringUtils.isNotNullString(clearLogEntity.getTableName())){
            resultT.setErrorMessage("表名不能为空");
            log.error("表名不能为空");

        }

    }
    public void calculateTime(ClearVo clearVo,ClearEntity clearEntity, ResultT<String> resultT){
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ReplaceVo replaceVo = new ReplaceVo();
            replaceVo.setMsg(clearEntity.getConditions());
            replaceVo.setDatabaseId(clearEntity.getParentId());
            replaceVo.setDataClassId(clearEntity.getDataClassId());
            replaceVo.setDdataId(clearEntity.getDdataId());
            replaceVo.setBackupTime(clearEntity.getTriggerLastTime());
            ExtractMessage.getIndexOf(replaceVo, resultT);
            clearVo.setConditions(replaceVo.getMsg());
            Set<Long> timeSet=replaceVo.getTimeSet();
            if(timeSet.size()==1){
                for (long time : timeSet) {
                    clearVo.setClearTime(time);
                    resultT.setSuccessMessage("资料删除时间为小于{}",format.format(time));
                    log.info("资料删除时间为小于{}",format.format(time));
                }
            }
        } catch (Exception e) {
           resultT.setErrorMessage("计算清除时间失败,{}",OwnException.get(e));
           log.error("计算清除时间失败,{}",OwnException.get(e));
        }

    }

    public ClearLogEntity insertClearLog(ClearEntity clearEntity,ClearLogEntity clearLogEntity,ResultT<String> resultT){
        try {
            clearLogEntity.setJobId(clearEntity.getId());
            clearLogEntity.setHandleCode("0");
            clearLogEntity.setTriggerCode(1);
            clearLogEntity.setHandleTime(new Date());
            clearLogEntity=clearLogService.saveNotNull(clearLogEntity);
            return clearLogEntity;
        }  catch (Exception e) {
            resultT.setErrorMessage("插入日志失败:{}", OwnException.get(e));
            log.error("插入日志失败:{}", OwnException.get(e));

            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            EiSendUtil.executeSqlException(resultT);
        }
        return null;


    }

    public void updateClearLog(ClearVo clearVo, ClearLogEntity clearLogEntity, ResultT<String> resultT){
        try {
            clearLogEntity.setHandleCode("1");
            clearLogEntity.setElapsedTime((clearVo.getEndTime()-clearVo.getStartTime())/1000);
            if(!resultT.isSuccess()){
                clearLogEntity.setHandleCode("2");
            }
            if(null==clearLogEntity.getHandleTime()){
                clearLogEntity.setHandleTime(new Date());
            }
            clearLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            clearLogService.saveNotNull(clearLogEntity);
        } catch (Exception e){
            resultT.setErrorMessage("修改日志出错{}",OwnException.get(e));
            resultT.setEiCode(ReturnCodeEnum.ReturnCodeEnum_13_ERROR.getKey());
            log.error("修改日志出错{}",OwnException.get(e));
            EiSendUtil.executeSqlException(resultT);
        }

    }


}

