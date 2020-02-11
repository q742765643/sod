package com.piesat.schedule.client.business;

import com.piesat.schedule.client.util.Select2File;
import com.piesat.schedule.entity.backup.BackupLogEntity;

import java.io.File;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-10 09:13
 **/
public class XuguBusiness {
    public void backUpKtable(BackupLogEntity backupLogEntity,Map<String,Object> map)  {
        StringBuilder sql=new StringBuilder();
        sql.append("select * from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions());
        Select2File select2File=new Select2File();
        String tempPath="/zzj/CMADASS/"+backupLogEntity.getFileName();
        map.put("tempPath",tempPath);
        if(!new File(tempPath).exists()){
            new File(tempPath).mkdirs();
        }
        String tempFilePath=tempPath+"/"+backupLogEntity.getFileName()+".exp";
        try {
            select2File.expparttab2(backupLogEntity.getTableName(),tempFilePath,sql,backupLogEntity.getParentId());
            map.put("kfilePath",tempFilePath);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void backUpVtable(BackupLogEntity backupLogEntity,Map<String,Object> map){
        String vfileName= (String) map.get("vfileName");
        StringBuilder sql=new StringBuilder();
        sql.append(" select a.* from ").append(backupLogEntity.getVTableName()).append(" a inner join");
        sql.append("(select * from ").append(backupLogEntity.getTableName()).append(" where ").append(backupLogEntity.getConditions()).append(") b");
        sql.append(" on a.").append(backupLogEntity.getForeignKey()).append("=b.").append(backupLogEntity.getForeignKey());
        Select2File select2File=new Select2File();
        String tempPath="/zzj/CMADASS/"+backupLogEntity.getFileName();
        map.put("tempPath",tempPath);
        if(!new File(tempPath).exists()){
            new File(tempPath).mkdirs();
        }
        String tempFilePath=tempPath+"/"+vfileName+".exp";
        try {
            select2File.expparttab2(backupLogEntity.getVTableName(),tempFilePath,sql,backupLogEntity.getParentId());
            map.put("vfilePath",tempFilePath);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

