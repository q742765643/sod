package com.piesat.schedule.client.business;

import com.piesat.schedule.client.util.Select2File;
import com.piesat.schedule.entity.backup.BackupLogEntity;

import java.io.File;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-10 09:13
 **/
public class XuguBusiness {
    public void backUpKtable(BackupLogEntity backupLogEntity,String conditions)  {
        StringBuilder sql=new StringBuilder();
        sql.append("select * from ").append(backupLogEntity.getTableName()).append(" where ").append(conditions);
        Select2File select2File=new Select2File();
        String tempPath="/zzj/CMADASS/"+backupLogEntity.getFileName();
        if(!new File(tempPath).exists()){
            new File(tempPath).mkdirs();
        }
        tempPath=tempPath+"/"+backupLogEntity.getFileName()+".exp";
        try {
            select2File.expparttab2(backupLogEntity.getTableName(),tempPath,sql,backupLogEntity.getParentId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void backUpVtable(BackupLogEntity backupLogEntity,String conditions){
        StringBuilder sql=new StringBuilder();
        sql.append(" select a.* from ").append(backupLogEntity.getVTableName()).append(" a inner join");
        sql.append("(select * from ").append(backupLogEntity.getTableName()).append(" ").append(conditions).append(") b");
        sql.append(" where a.").append(backupLogEntity.getForeignKey()).append("=b.").append(backupLogEntity.getForeignKey());

    }
}

