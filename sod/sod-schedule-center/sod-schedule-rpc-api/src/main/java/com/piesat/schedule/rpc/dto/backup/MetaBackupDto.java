package com.piesat.schedule.rpc.dto.backup;

import com.piesat.schedule.rpc.dto.JobInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-26 16:01
 **/
@Data
@ApiModel("元数据备份DTO")
public class MetaBackupDto extends JobInfoDto{
    @ApiModelProperty("物理库ID")
    private String databaseId;
    @ApiModelProperty("0 结构 1数据")
    private String isStructure;
    @ApiModelProperty("备份树json")
    private String backContent;
    @ApiModelProperty("基础库ID")
    private String parentId;
    @ApiModelProperty("物理库名称")
    private String databaseName;
    @ApiModelProperty("存储目录")
    private String storageDirectory;
    @ApiModelProperty("where条件")
    private String conditions;

}

