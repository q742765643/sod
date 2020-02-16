package com.piesat.schedule.client.handler.clear;

import com.piesat.schedule.client.business.GbaseBusiness;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.clear.ClearLogService;
import com.piesat.schedule.client.util.ExtractMessage;
import com.piesat.schedule.client.vo.BackupVo;
import com.piesat.schedule.client.vo.ClearVo;
import com.piesat.schedule.client.vo.ReplaceVo;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        log.info("清除调用成功");
        ClearEntity clearEntity= (ClearEntity) jobInfoEntity;
        ResultT<String> resultT=new ResultT<>();
        this.preParam(clearEntity,resultT);
    }

    public void preParam( ClearEntity clearEntity, ResultT<String> resultT) {
        ClearLogEntity clearLogEntity=new ClearLogEntity();
        BeanUtils.copyProperties(clearEntity,clearLogEntity);
        clearLogEntity.setId(null);
        ClearVo clearVo=this.calculateTime(clearEntity,resultT);
        clearLogEntity.setConditions(clearVo.getConditions());
        this.insertClearLog(clearEntity,clearLogEntity,resultT);
        GbaseBusiness gbaseBusiness=new GbaseBusiness();
        gbaseBusiness.deleteKtable(clearLogEntity,clearVo,resultT);
        System.out.println();

    }

    public ClearVo calculateTime(ClearEntity clearEntity, ResultT<String> resultT){
        ClearVo clearVo = new ClearVo();
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
            }
        }
        return clearVo;

    }

    public void insertClearLog(ClearEntity clearEntity,ClearLogEntity clearLogEntity,ResultT<String> resultT){
        clearLogEntity.setJobId(clearEntity.getId());
        clearLogEntity.setHandleCode("0");
        clearLogEntity.setTriggerCode(1);
        clearLogEntity.setHandleTime(new Date());
        clearLogService.saveNotNull(clearLogEntity);


    }
}
