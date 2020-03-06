package com.piesat.schedule.rpc.dto.recover;

import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-05 16:28
 **/
@Data
public class RecoverLogDto extends JobInfoLogDto{
    private String databaseId;
    private String dataClassId;
    private String profileName;
    private String ddataId;
    private String storageDirectory;
    private String tableName;
    private String vTableName;
    private String databaseType;
    private String parentId;
}

