package com.piesat.schedule.rpc.dto.clear;

import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 16:53
 **/
@Data
public class MetaClearLogDto extends JobInfoLogDto{
    private String databaseId;
    private String clearContent;
    private String parentId;
    private String databaseName;
    private String conditions;
    private String databaseType;
}

