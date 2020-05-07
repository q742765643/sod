package com.piesat.schedule.rpc.enums;

import com.piesat.schedule.constant.HandleConstant;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.move.MoveEntity;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 16:44
 **/
public enum ExecuteEnum {
    BACKUP(HandleConstant.BACKUP,"executeBackupService"),
    MOVE(HandleConstant.MOVE,"executeMoveService"),
    CLEAR(HandleConstant.CLEAR,"executeClearService"),
    METABACKUP(HandleConstant.METABACKUP,"executeMetaBackupService"),
    METACLEAR(HandleConstant.METACLEAR,"executeMetaClearService"),
    MMD(HandleConstant.MMD,"executeMmdSyncService"),
    JOB(HandleConstant.JOB,"executeJobService");


    private String name;
    private String service;
    ExecuteEnum(String name, String service) {
        this.name = name;
        this.service = service;
    }

    public static String getService(String name){
        if (name != null) {
            for (ExecuteEnum item : ExecuteEnum.values()) {
                if (item.name.equals(name)) {
                    return item.service;
                }
            }
        }
        return null;
    }
}

