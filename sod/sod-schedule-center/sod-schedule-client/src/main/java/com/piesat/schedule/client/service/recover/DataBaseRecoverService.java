package com.piesat.schedule.client.service.recover;

import com.alibaba.fastjson.JSON;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.util.fetl.type.Type;
import com.piesat.schedule.client.vo.MetadataVo;
import com.piesat.schedule.client.vo.RecoverMetaVo;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupLogEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.sql.ResultSet;
import java.util.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 13:04
 **/
@Slf4j
@Service
public class DataBaseRecoverService {
    @Autowired
    private MetaRecoverLogService metaRecoverLogService;

    public void recover(MetaRecoverLogEntity recoverLogEntity){
        ResultT<String> resultT=new ResultT();
        RecoverMetaVo recoverMetaVo=new RecoverMetaVo();
        recoverMetaVo.setStartTime(System.currentTimeMillis());
        try {
            recoverLogEntity=this.insertMetaBackupLog(recoverLogEntity,resultT);
            if(!resultT.isSuccess()){
                return;
            }
            this.parsingMetadata(recoverMetaVo,recoverLogEntity,resultT);
            Map<Type, Set<String>> impInfo= this.toMap(recoverMetaVo,recoverLogEntity,resultT);
            File file=new File(recoverLogEntity.getStorageDirectory());
            String fileName=file.getName().substring(0,file.getName().lastIndexOf("."));
            String unzipPath=file.getParentFile().getPath()+'/'+fileName;
            recoverMetaVo.setUnzipPath(unzipPath);
            ZipUtils.unZip(new File(recoverLogEntity.getStorageDirectory()),unzipPath);
            if(!resultT.isSuccess()){
                return;
            }
            BusinessEnum businessEnum = BusinessEnum.match(recoverLogEntity.getDatabaseType(), null);
            BaseBusiness baseBusiness = businessEnum.getBaseBusiness();
            baseBusiness.recoverMeta(recoverMetaVo,impInfo,recoverLogEntity,resultT);
            recoverMetaVo.setIndexPath(unzipPath+"/index.sql");
        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
            log.error(OwnException.get(e));
        } finally {
            recoverMetaVo.setEndTime(System.currentTimeMillis());
            FileUtil.delFile(new File(recoverMetaVo.getUnzipPath()),resultT);
            this.updateMetaRecoverLogEntity(recoverLogEntity,recoverMetaVo,resultT);
        }

    }
    public void parsingMetadata(RecoverMetaVo recoverMetaVo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT){
        List<TreeVo> treeVos= JSON.parseArray(recoverLogEntity.getRecoverContent(),TreeVo.class);
        for(TreeVo treeVo:treeVos) {
            if (treeVo.isParent() && treeVo.getPId().indexOf("数据库") < 0) {
                continue;
            }
            if (treeVo.getPId().indexOf("数据库") != -1) {
                recoverMetaVo.getSchema().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("用户") != -1) {
                recoverMetaVo.getUsers().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("角色") != -1) {
                recoverMetaVo.getRoles().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("表") != -1) {
                recoverMetaVo.getTable().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("视图") != -1) {
                recoverMetaVo.getView().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("序列") != -1) {
                recoverMetaVo.getSequence().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("触发器") != -1) {
                recoverMetaVo.getTrigger().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("存储过程") != -1) {
                recoverMetaVo.getProcedure().add(treeVo.getId());
            }

        }
    }
    public  Map<Type, Set<String>>  toMap(RecoverMetaVo recoverMetaVo, MetaRecoverLogEntity recoverLogEntity, ResultT<String> resultT){
        Map<Type, Set<String>> impInfo = new HashMap<>();
        if(!recoverMetaVo.getSchema().isEmpty()){
            impInfo.put(Type.SCHEMA, recoverMetaVo.getSchema());
        }
        if(!recoverMetaVo.getUsers().isEmpty()){
            impInfo.put(Type.USER, recoverMetaVo.getUsers());
        }
        if(!recoverMetaVo.getRoles().isEmpty()){
            impInfo.put(Type.ROLE,recoverMetaVo.getRoles());
        }
        if(!recoverMetaVo.getView().isEmpty()){
            impInfo.put(Type.VIEW,recoverMetaVo.getView());
        }
        if(!recoverMetaVo.getProcedure().isEmpty()){
            impInfo.put(Type.PROCEDURE,recoverMetaVo.getProcedure());
        }
        if(!recoverMetaVo.getSequence().isEmpty()){
            impInfo.put(Type.SEQUENCE,recoverMetaVo.getSequence());
        }
        if(!recoverMetaVo.getTrigger().isEmpty()){
            impInfo.put(Type.TRIGGER,recoverMetaVo.getTrigger());
        }
        if(!recoverMetaVo.getTable().isEmpty()){
            if(recoverLogEntity.getIsStructure().indexOf("0")!=-1){
                impInfo.put(Type.TABLE,recoverMetaVo.getTable());
            }
            if(recoverLogEntity.getIsStructure().indexOf("1")!=-1){
                impInfo.put(Type.DATA,recoverMetaVo.getTable());
            }
        }
        return impInfo;
    }

    public MetaRecoverLogEntity insertMetaBackupLog(MetaRecoverLogEntity metaRecoverLogEntity, ResultT<String> resultT){
        try {

            metaRecoverLogEntity.setHandleCode("0");
            metaRecoverLogEntity.setTriggerCode(1);
            metaRecoverLogEntity.setHandleTime(new Date());
            metaRecoverLogEntity=metaRecoverLogService.saveNotNull(metaRecoverLogEntity);
            return metaRecoverLogEntity;
        }  catch (Exception e) {
            resultT.setErrorMessage("插入日志失败:{}", OwnException.get(e));
            log.error("插入日志失败:{}", OwnException.get(e));
        }
        return null;


    }

    public void updateMetaRecoverLogEntity(MetaRecoverLogEntity metaRecoverLogEntity, RecoverMetaVo recoverMetaVo, ResultT<String> resultT){
        try {
            metaRecoverLogEntity.setHandleCode("1");
            metaRecoverLogEntity.setElapsedTime((recoverMetaVo.getEndTime()-recoverMetaVo.getStartTime())/1000);
            if(!resultT.isSuccess()){
                metaRecoverLogEntity.setHandleCode("2");
            }
            metaRecoverLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            metaRecoverLogService.saveNotNull(metaRecoverLogEntity);
        } catch (Exception e){
            resultT.setErrorMessage("修改日志出错{}",OwnException.get(e));
            log.error("修改日志出错{}",OwnException.get(e));
        }

    }
}

