package com.piesat.schedule.rpc.dto.clear;

import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-29 19:08
 **/
@Data
public class ClearLogDto extends JobInfoLogDto{
    private String databaseId;
    private String dataClassId;
    private String profileName;
    private String ddataId;
    private String tableName;
    private String conditions;
    private long clearLimit;
    private String vTableName;
    private long clearCount;
    private String databaseType;
    private String parentId;
    private String foreignKey;
    private long clearTime;
}

