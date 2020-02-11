package com.piesat.schedule.client.business;

import com.piesat.schedule.client.util.Select2File;
import com.piesat.schedule.client.util.ZipUtils;
import com.piesat.schedule.client.util.fetl.exp.ExpMetadata;
import com.piesat.schedule.client.vo.StrategyVo;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import com.piesat.util.ResultT;

import java.io.File;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-10 09:13
 **/
public class XuguBusiness extends BaseBusiness{
    @Override
    public void backUpKtable(BackupLogEntity backupLogEntity, StrategyVo strategyVo, ResultT<String> resultT)  {
        StringBuilder sql=new StringBuilder();
        sql.append("select * from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions());
        Select2File select2File=new Select2File();

        String tempFilePath=strategyVo.getTempPtah()+"/"+backupLogEntity.getFileName()+".exp";
        try {
            select2File.expparttab2(backupLogEntity.getTableName(),tempFilePath,sql,backupLogEntity.getParentId());
            strategyVo.setKfilePath(tempFilePath);
            ExpMetadata expMetadata=new ExpMetadata();
            File tempFile=new File(tempFilePath);
            String index=expMetadata.expData(tempFile.getName(),backupLogEntity.getTableName(),sql.toString(),backupLogEntity.getParentId());
            ZipUtils.writetxt(strategyVo.getIndexPath(),index,resultT);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public void backUpVtable(BackupLogEntity backupLogEntity,StrategyVo strategyVo,ResultT<String> resultT){
        StringBuilder sql=new StringBuilder();
        sql.append(" select a.* from ").append(backupLogEntity.getVTableName()).append(" a inner join");
        sql.append("(select * from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions()).append(") b");
        sql.append(" on a.").append(backupLogEntity.getForeignKey()).append("=b.").append(backupLogEntity.getForeignKey());
        Select2File select2File=new Select2File();
        String tempFilePath=strategyVo.getTempPtah()+"/"+strategyVo.getVfileName()+".exp";
        try {
            select2File.expparttab2(backupLogEntity.getVTableName(),tempFilePath,sql,backupLogEntity.getParentId());
            strategyVo.setVfilePath(tempFilePath);
            File tempFile=new File(tempFilePath);
            ExpMetadata expMetadata=new ExpMetadata();
            String index=expMetadata.expData(tempFile.getName(),backupLogEntity.getVTableName(),sql.toString(),backupLogEntity.getParentId());
            ZipUtils.writetxt(strategyVo.getIndexPath(),index,resultT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

