package com.piesat.schedule.rpc.dto.backup;

import com.piesat.schedule.rpc.dto.JobInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;


/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 16:54
 **/
@Data
@ApiModel("数据备份DTO")
public class BackUpDto extends JobInfoDto{
    @ApiModelProperty("物理库id")
    private String databaseId;
    @ApiModelProperty("存储编码")
    private String dataClassId;
    @ApiModelProperty("资料名称")
    private String profileName;
    @ApiModelProperty("四级编码")
    private String ddataId;
    @ApiModelProperty("近时备份条件")
    private String conditions;
    @ApiModelProperty("远时备份条件")
    private String secondConditions;
    @ApiModelProperty("存储目录")
    private String storageDirectory;
    @ApiModelProperty("K表名")
    private String tableName;
    @ApiModelProperty("V表名")
    private String vTableName;
    @ApiModelProperty("物理库类型")
    private String databaseType;
    @ApiModelProperty("物理库基础库ID")
    private String parentId;
    @ApiModelProperty("外键")
    private String foreignKey;
    private Date backupStartTime;

}

