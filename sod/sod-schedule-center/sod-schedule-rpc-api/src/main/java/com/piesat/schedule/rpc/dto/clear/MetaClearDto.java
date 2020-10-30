package com.piesat.schedule.rpc.dto.clear;

import com.piesat.schedule.rpc.dto.JobInfoDto;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 10:50
 **/
@Data
public class MetaClearDto extends JobInfoDto{
    private String databaseId;
    private String clearContent;
    private String parentId;
    private String databaseName;
    private String conditions;
    private String databaseType;
    private String partiOff;
}

