package com.piesat.schedule.rpc.dto.backup;

import com.piesat.schedule.rpc.dto.JobInfoDto;
import lombok.Data;


/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 16:54
 **/
@Data
public class BackUpDto extends JobInfoDto{
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

