package com.piesat.schedule.rpc.dto.backup;

import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 10:00
 **/
@Data
public class MetaBackupLogDto extends JobInfoLogDto {
    @ApiModelProperty("物理库id")
    private String databaseId;
    @ApiModelProperty("0 结构 1数据")
    private String isStructure;
    @ApiModelProperty("备份内容")
    private String backContent;
    @ApiModelProperty("物理库基础ID")
    private String parentId;
    @ApiModelProperty("物理库内容")
    private String databaseName;
    @ApiModelProperty("存储目录")
    private String storageDirectory;
    @ApiModelProperty("条件")
    private String conditions;
}

