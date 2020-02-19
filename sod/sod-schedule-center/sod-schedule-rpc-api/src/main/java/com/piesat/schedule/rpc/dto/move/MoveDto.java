package com.piesat.schedule.rpc.dto.move;

import com.piesat.schedule.rpc.dto.JobInfoDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 16:25
 **/
@Data
@ApiModel("数据迁移DTO")
public class MoveDto extends JobInfoDto{
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
    @ApiModelProperty("迁移条件")
    private String conditions;
    @ApiModelProperty("二级NAS清除条件")
    private String clearConditions;
    private String archiveConditions;
    @ApiModelProperty("迁移源目录")
    private String sourceDirectory;
    @ApiModelProperty("迁移目标目录")
    private String targetDirectory;
    @ApiModelProperty("是否进行二级NAS清除")
    private String isClear;
    @ApiModelProperty("迁移频率")
    private long moveLimit;
    @ApiModelProperty("物理库类型")
    private String databaseType;
    @ApiModelProperty("物理库基础库ID")
    private String parentId;
    @ApiModelProperty("外键")
    private String foreignKey;
}

