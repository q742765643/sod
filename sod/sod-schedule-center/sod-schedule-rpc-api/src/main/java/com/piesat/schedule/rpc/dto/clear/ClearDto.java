package com.piesat.schedule.rpc.dto.clear;

import com.piesat.schedule.rpc.dto.JobInfoDto;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 11:11
 **/
@Data
public class ClearDto extends JobInfoDto {
    private String databaseId;
    private String dataClassId;
    private String profileName;
    private String ddataId;
    private String tableName;
    private String conditions;
    private long clearLimit;
}

