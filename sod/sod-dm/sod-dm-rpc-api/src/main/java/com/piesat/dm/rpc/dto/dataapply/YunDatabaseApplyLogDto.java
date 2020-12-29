package com.piesat.dm.rpc.dto.dataapply;


import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("中间件变更日志Dto")
public class YunDatabaseApplyLogDto extends BaseDto {




    /**
     * ID
     */
    @ApiModelProperty("ID")
    private String logId;

    /**
     * 单位审核材料
     */
    @ApiModelProperty("单位审核材料")
    private String examineMaterial;

    /**
     * 数据库用途
     */
    @ApiModelProperty("数据库用途")
    private String databaseUse;

    /**
     * 审核状态
     * 包括：01-待审、02-已审、03-拒绝
     */
    @ApiModelProperty("审核状态")
    private String examineStatus;

    /**
     * 显示状态
     * 包括：1-申请、2-审核通过、3-管理员修改、
     */
    @ApiModelProperty("审核状态")
    private String viewStatus;
    /**
     * 拒绝原因
     */
    @ApiModelProperty("拒绝原因")
    private String rejectReason;
    /**
     * 申请用户id
     */
    @ApiModelProperty( "申请用户ID")
    private String userId;

}
