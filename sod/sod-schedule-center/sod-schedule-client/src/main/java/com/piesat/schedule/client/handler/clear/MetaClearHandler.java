package com.piesat.schedule.client.handler.clear;

import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.business.XuguBusiness;
import com.piesat.schedule.client.datasource.DataSourceContextHolder;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.DatabaseOperationService;
import com.piesat.schedule.client.service.clear.MetaClearLogService;
import com.piesat.schedule.client.util.ExtractMessage;
import com.piesat.schedule.client.vo.ClearVo;
import com.piesat.schedule.client.vo.MetadataVo;
import com.piesat.schedule.client.vo.ReplaceVo;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupLogEntity;
import com.piesat.schedule.entity.clear.MetaClearEntity;
import com.piesat.schedule.entity.clear.MetaClearLogEntity;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 16:36
 **/
@Slf4j
@Service("metaclearHandler")
public class MetaClearHandler implements BaseHandler {
    @Autowired
    private MetaClearLogService metaClearLogService;
    @Autowired
    private DatabaseOperationService databaseOperationService;
    @Override
    public void execute(JobInfoEntity jobInfoEntity,ResultT<String> resultT) {
        MetaClearEntity metaClearEntity= (MetaClearEntity) jobInfoEntity;
        this.preParam(metaClearEntity,resultT);
    }

    public void preParam(MetaClearEntity metaClearEntity, ResultT<String> resultT){
        MetaClearLogEntity metaClearLogEntity=new MetaClearLogEntity();
        long startTime=System.currentTimeMillis();
        try {
            BeanUtils.copyProperties(metaClearEntity,metaClearLogEntity);
            metaClearLogEntity.setId(null);
            metaClearLogEntity.setJobId(metaClearEntity.getId());
            metaClearLogEntity.setTriggerTime(metaClearEntity.getTriggerLastTime());

            /*ReplaceVo replaceVo = new ReplaceVo();
            replaceVo.setMsg(metaClearEntity.getConditions());
            replaceVo.setBackupTime(metaClearEntity.getTriggerLastTime());
            ExtractMessage.getIndexOf(replaceVo, resultT);*/
            ClearVo clearVo = new ClearVo();
            clearVo.setStartTime(System.currentTimeMillis());
            this.calculateTime(clearVo,metaClearEntity,resultT);

            resultT.setSuccessMessage("清除条件为{}",metaClearEntity.getConditions());
            metaClearLogEntity=this.insertMetaClearLog(metaClearLogEntity,resultT);
            metaClearLogEntity.setConditions(metaClearEntity.getConditions());

            if(metaClearEntity.getPartiOff() != null && metaClearEntity.getPartiOff().equals("1")){
                //删分区
                this.metaPartiClearExecute(metaClearLogEntity,clearVo,resultT);
            }else{
                //删数据
                this.metaClearExecute(metaClearLogEntity,resultT);
            }
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage("清除失败,原因:{}",OwnException.get(e));
        }finally {
            this.updateMetaClearLog(metaClearLogEntity,startTime,resultT);
        }


    }
    public void metaClearExecute(MetaClearLogEntity metaClearLogEntity,ResultT<String> resultT){
        DataSourceContextHolder.setDataSource(metaClearLogEntity.getParentId());
        String[] tableNames=metaClearLogEntity.getClearContent().split(",");
        if(tableNames.length<=0){
            resultT.setSuccessMessage("没有表需要进行清除");
            return;
        }
        for(String tableName:tableNames){
            resultT.setSuccessMessage("开始清除表{}",tableName);
            try {
                databaseOperationService.delteKtable(tableName,metaClearLogEntity.getConditions());
            } catch (Exception e) {
                resultT.setErrorMessage("清除表{}失败,原因:{}",tableName,OwnException.get(e));
                log.error(OwnException.get(e));
            }

        }
        DataSourceContextHolder.clearDataSource();

    }

    public void metaPartiClearExecute(MetaClearLogEntity metaClearLogEntity, ClearVo clearVo,ResultT<String> resultT){
        DataSourceContextHolder.setDataSource(metaClearLogEntity.getParentId());
        String[] tableNames=metaClearLogEntity.getClearContent().split(",");
        if(tableNames.length<=0){
            resultT.setSuccessMessage("没有表需要进行清除");
            return;
        }
        for(String tableName:tableNames){
            resultT.setSuccessMessage("开始执行虚谷删除表{}分区",tableName);
            try {
                XuguBusiness xuguBusiness = new XuguBusiness();
                xuguBusiness.deleteMetaParti(tableName,clearVo,metaClearLogEntity,resultT);
            } catch (Exception e) {
                resultT.setErrorMessage("执行虚谷删除表{}分区失败,原因:{}",tableName,OwnException.get(e));
                log.error("执行虚谷删除分区失败:{}",OwnException.get(e));
            }

        }
        DataSourceContextHolder.clearDataSource();
    }

    public MetaClearLogEntity insertMetaClearLog( MetaClearLogEntity metaClearLogEntity, ResultT<String> resultT){
        try {
            metaClearLogEntity.setHandleCode("0");
            metaClearLogEntity.setTriggerCode(1);
            metaClearLogEntity.setHandleTime(new Date());
            metaClearLogEntity=metaClearLogService.saveNotNull(metaClearLogEntity);
            return metaClearLogEntity;
        }  catch (Exception e) {
            resultT.setErrorMessage("插入日志失败:{}", OwnException.get(e));
            log.error("插入日志失败:{}", OwnException.get(e));
        }
        return null;


    }

    public void updateMetaClearLog(MetaClearLogEntity metaClearLogEntity,long startTime, ResultT<String> resultT){
        try {
            metaClearLogEntity.setHandleCode("1");
            metaClearLogEntity.setElapsedTime((System.currentTimeMillis()-startTime)/1000);
            if(!resultT.isSuccess()){
                metaClearLogEntity.setHandleCode("2");
            }
            metaClearLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            metaClearLogService.saveNotNull(metaClearLogEntity);
        } catch (Exception e){
            resultT.setErrorMessage("修改日志出错{}",OwnException.get(e));
            log.error("修改日志出错{}",OwnException.get(e));
        }

    }

    public void calculateTime(ClearVo clearVo,MetaClearEntity metaClearEntity, ResultT<String> resultT){
        try {
            SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ReplaceVo replaceVo = new ReplaceVo();
            replaceVo.setMsg(metaClearEntity.getConditions());
            replaceVo.setDatabaseId(metaClearEntity.getParentId());
            replaceVo.setBackupTime(metaClearEntity.getTriggerLastTime());
            ExtractMessage.getIndexOf(replaceVo, resultT);
            clearVo.setConditions(replaceVo.getMsg());
            metaClearEntity.setConditions(replaceVo.getMsg());
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

}

