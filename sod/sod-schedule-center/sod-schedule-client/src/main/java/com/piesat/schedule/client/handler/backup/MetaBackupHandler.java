package com.piesat.schedule.client.handler.backup;

import com.alibaba.fastjson.JSON;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.vo.MetadataVo;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.backup.MetaBackupEntity;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        MetaBackupEntity metaBackupEntity= (MetaBackupEntity) jobInfoEntity;

    }

    public void preParam(MetaBackupEntity metaBackupEntity, ResultT<String> resultT) {

    }

    public void parsingMetadata(MetaBackupEntity metaBackupEntity,ResultT<String> resultT){
        MetadataVo metadataVo=new MetadataVo();
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


}

