package com.piesat.dm.rpc.dto.dataapply;


import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data
@ApiModel("中间件反馈Dto")
public class YunDatabaseApplyFeedbackDto extends BaseDto {


    /**
     * 实例信息ID
     */
    @ApiModelProperty("实例信息ID")
    private String feedbackId;
    /**
     * 实例ID
     */
    @ApiModelProperty("实例ID")
    private String itserviceId;
    /**
     * 实例类型
     */
    @ApiModelProperty("实例类型")
    private String storageLogic;
    /**
     * 反馈材料
     */
    @ApiModelProperty("反馈材料")
    private String material;
    /**
     * 处理材料
     */
    @ApiModelProperty("处理材料")
    private String handleMaterial;

    /**
     *标题
     */
    @ApiModelProperty("标题")
    private String feedbackTitle;
    /**
     * 反馈处理状态（1-未接手，2-已接手，3-已处理）
     */
    @ApiModelProperty("反馈处理状态")
    private String feedbackStatus;
    /**
     * 名称
     */
    @ApiModelProperty("名称")
    private String displayname;
    /**
     * 反馈用户id
     */
    @ApiModelProperty( "反馈用户ID")
    private String userId;
    /**
     * 处理人
     */
    @ApiModelProperty("处理人")
    private String processor;

}
