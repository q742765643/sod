package com.piesat.schedule.rpc.dto.move;

import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-29 19:09
 **/
@Data
public class MoveLogDto extends JobInfoLogDto{
    private String databaseId;
    private String dataClassId;
    private String profileName;
    private String ddataId;
    private String tableName;
    private String conditions;
    private String clearConditions;
    private String archiveConditions;
    private String sourceDirectory;
    private String targetDirectory;
    private String isClear;
    private long moveLimit;
    private String vTableName;
    private long moveCount;
    private String databaseType;
    private String parentId;
    private String foreignKey;
    private long moveTime;


}

