package com.piesat.schedule.rpc.dto.backup;

import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-29 19:05
 **/
@Data
public class BackupLogDto extends JobInfoLogDto {
    private String databaseId;
    private String dataClassId;
    private String profileName;
    private String ddataId;
    private String conditions;
    private String secondConditions;
    private String storageDirectory;
    private String tableName;
    private String vTableName;
    private String databaseType;
    private String parentId;
}

