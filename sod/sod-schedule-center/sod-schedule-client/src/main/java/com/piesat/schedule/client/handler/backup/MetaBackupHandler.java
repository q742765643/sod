package com.piesat.schedule.client.handler.backup;

import com.alibaba.fastjson.JSON;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.backup.MetaBackupLogService;
import com.piesat.schedule.client.util.EiSendUtil;
import com.piesat.schedule.client.util.ExtractMessage;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.vo.ClearVo;
import com.piesat.schedule.client.vo.MetadataVo;
import com.piesat.schedule.client.vo.ReplaceVo;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupLogEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.clear.ClearLogEntity;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-27 09:35
 **/
@Slf4j
@Service("metabackupHandler")
public class MetaBackupHandler implements BaseHandler{
    @Autowired
    private MetaBackupLogService metaBackupLogService;
    @Override
    public void execute(JobInfoEntity jobInfoEntity,ResultT<String> resultT) {
        MetaBackupEntity metaBackupEntity= (MetaBackupEntity) jobInfoEntity;
        this.preParam(metaBackupEntity,resultT);
    }

    public void preParam(MetaBackupEntity metaBackupEntity, ResultT<String> resultT) {
        MetadataVo metadataVo=new MetadataVo();
        metadataVo.setStartTime(System.currentTimeMillis());
        MetaBackupLogEntity metaBackupLogEntity=new MetaBackupLogEntity();
        BeanUtils.copyProperties(metaBackupEntity,metaBackupLogEntity);
        metaBackupLogEntity.setTriggerTime(metaBackupEntity.getTriggerLastTime());
        metaBackupLogEntity.setId(null);
        try {
            metaBackupLogEntity=this.insertMetaBackupLog(metaBackupLogEntity,metaBackupEntity,resultT);
            this.parsingMetadata(metadataVo,metaBackupEntity,resultT);
            if(metaBackupEntity.getIsStructure().indexOf("1")!=-1){
                metadataVo.setExpData(true);
            }
            if(metaBackupEntity.getParentId().indexOf("META")<0&&metadataVo.getTable().size()>1){
                metadataVo.setExpData(false);
            }
            resultT.setSuccessMessage("获取备份内容");
            ReplaceVo replaceVo = new ReplaceVo();
            replaceVo.setMsg(metaBackupEntity.getStorageDirectory() + "/{databaseId}/{yyyy}/{yyyy-MM}/"+metaBackupEntity.getTaskName()+"_{yyyyMMddHHmmss}");
            replaceVo.setDatabaseId(metaBackupEntity.getParentId());
            replaceVo.setBackupTime(System.currentTimeMillis());
            ExtractMessage.getIndexOf(replaceVo, resultT);
            metadataVo.setParentPath(replaceVo.getMsg());
            metadataVo.setZipPath(replaceVo.getMsg()+".zip");
            resultT.setSuccessMessage("创建备份目录{}",metadataVo.getParentPath());
            FileUtil.mkdirs(metadataVo.getParentPath(),resultT);
            if(!resultT.isSuccess()){
                return;
            }
            ZipUtils.writetxt(metadataVo.getParentPath()+"/tree.txt",metaBackupEntity.getBackContent(),resultT);
            ZipUtils.writetxt(metadataVo.getParentPath()+"/structure.txt",metaBackupEntity.getIsStructure(),resultT);
            metadataVo.setIndexPath(replaceVo.getMsg()+"/index.sql");
            BusinessEnum businessEnum = BusinessEnum.match(metaBackupEntity.getDatabaseType(), null);
            BaseBusiness baseBusiness = businessEnum.getBaseBusiness();
            baseBusiness.metaBack(metaBackupEntity,metadataVo,resultT);
            resultT.setSuccessMessage("压缩备份目录{}",replaceVo.getMsg());
            ZipUtils.doCompress(replaceVo.getMsg(),replaceVo.getMsg()+".zip",resultT);

        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
            log.info("元数据备份失败{}",OwnException.get(e));
        } finally {
            FileUtil.delFile(new File(metadataVo.getParentPath()),resultT);
            metadataVo.setEndTime(System.currentTimeMillis());
            this.updateMetaBackupLog(metaBackupLogEntity,metadataVo,resultT);
        }

    }

    public void parsingMetadata(MetadataVo metadataVo,MetaBackupEntity metaBackupEntity,ResultT<String> resultT){
        List<TreeVo> treeVos= JSON.parseArray(metaBackupEntity.getBackContent(),TreeVo.class);
        for(TreeVo treeVo:treeVos) {
            if (treeVo.isParent() && treeVo.getPId().indexOf("数据库") < 0) {
                continue;
            }
            if (treeVo.getPId().indexOf("数据库") != -1) {
                metadataVo.getSchema().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("用户") != -1) {
                metadataVo.getUsers().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("角色") != -1) {
                metadataVo.getRoles().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("表") != -1) {
                metadataVo.getTable().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("视图") != -1) {
                metadataVo.getView().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("序列") != -1) {
                metadataVo.getSequence().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("触发器") != -1) {
                metadataVo.getTrigger().add(treeVo.getId());
            }
            if (treeVo.getPId().indexOf("存储过程") != -1) {
                metadataVo.getProcedure().add(treeVo.getId());
            }

        }
    }
    public MetaBackupLogEntity insertMetaBackupLog( MetaBackupLogEntity metaBackupLogEntity,MetaBackupEntity metaBackupEntity,ResultT<String> resultT){
        try {

            metaBackupLogEntity.setJobId(metaBackupEntity.getId());
            metaBackupLogEntity.setHandleCode("0");
            metaBackupLogEntity.setTriggerCode(1);
            metaBackupLogEntity.setHandleTime(new Date());
            metaBackupLogEntity=metaBackupLogService.saveNotNull(metaBackupLogEntity);
            return metaBackupLogEntity;
        }  catch (Exception e) {
            resultT.setErrorMessage("插入日志失败:{}", OwnException.get(e));
            log.error("插入日志失败:{}", OwnException.get(e));
        }
        return null;


    }

    public void updateMetaBackupLog(MetaBackupLogEntity metaBackupLogEntity, MetadataVo metadataVo, ResultT<String> resultT){
        try {
            metaBackupLogEntity.setHandleCode("1");
            metaBackupLogEntity.setElapsedTime((metadataVo.getEndTime()-metadataVo.getStartTime())/1000);
            metaBackupLogEntity.setStorageDirectory(metadataVo.getZipPath());
            if(!resultT.isSuccess()){
                metaBackupLogEntity.setHandleCode("2");
                //metaBackupLogEntity.setStorageDirectory("");
            }
            metaBackupLogEntity.setHandleMsg(resultT.getProcessMsg().toString());
            metaBackupLogService.saveNotNull(metaBackupLogEntity);
        } catch (Exception e){
            metaBackupLogService.saveNotNull(metaBackupLogEntity);
            //resultT.setErrorMessage("修改日志出错{}",OwnException.get(e));
            log.error("修改日志出错{}",OwnException.get(e));
        }

    }

}

