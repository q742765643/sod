package com.piesat.schedule.rpc.dto.recover;

import com.piesat.schedule.rpc.dto.JobInfoLogDto;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-03 10:17
 **/
@Data
public class MetaRecoverLogDto extends JobInfoLogDto {
    @ApiModelProperty("物理库id")
    private String databaseId;
    @ApiModelProperty("物理库类型")
    private String databaseType;
    @ApiModelProperty("0 结构 1数据")
    private String isStructure;
    @ApiModelProperty("恢复内容 树JSON")
    private String recoverContent;
    @ApiModelProperty("物理库基础ID")
    private String parentId;
    @ApiModelProperty("物理库名称")
    private String databaseName;
    @ApiModelProperty("恢复文件路径")
    private String storageDirectory;
}

