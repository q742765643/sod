package com.piesat.schedule.rpc.dto.move;

import com.piesat.schedule.rpc.dto.JobInfoDto;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 16:25
 **/
@Data
public class MoveDto extends JobInfoDto{
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

}

