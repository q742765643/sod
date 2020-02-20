package com.piesat.schedule.rpc.dto.clear;

import com.piesat.schedule.rpc.dto.JobInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 11:11
 **/
@Data
@ApiModel("数据清除DTO")
public class ClearDto extends JobInfoDto {
    @ApiModelProperty("物理库id")
    private String databaseId;
    @ApiModelProperty("存储编码")
    private String dataClassId;
    @ApiModelProperty("资料名称")
    private String profileName;
    @ApiModelProperty("四级编码")
    private String ddataId;
    @ApiModelProperty("K表名")
    private String tableName;
    @ApiModelProperty("V表名")
    private String vTableName;
    @ApiModelProperty("清除条件")
    private String conditions;
    @ApiModelProperty("清除频率")
    private long clearLimit;
    @ApiModelProperty("物理库类型")
    private String databaseType;
    @ApiModelProperty("物理库基础库ID")
    private String parentId;
    @ApiModelProperty("外键")
    private String foreignKey;
}

