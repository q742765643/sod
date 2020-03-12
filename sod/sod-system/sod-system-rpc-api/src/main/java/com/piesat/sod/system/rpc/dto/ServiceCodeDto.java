package com.piesat.sod.system.rpc.dto;

import com.piesat.util.BaseDto;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/13 15:11
 */
@Data
@ApiModel("服务代码管理DTO")
public class ServiceCodeDto extends BaseDto {

    /**
     * 服务代码
     */
    @ApiModelProperty("服务代码")
    private String userEleCode;

    /**
     * 字段编码
     */
    @ApiModelProperty("字段编码")
    private String dbEleCode;

    /**
     * 要素中文名称
     */
    @ApiModelProperty("要素中文名称")
    private String eleName;

    /**
     * 描述
     */
    @ApiModelProperty("描述")
    private String description;

    /**
     *
     */
    @ApiModelProperty("")
    private String hasSod;

    /**
     *  单位
     */
    @ApiModelProperty("单位")
    private String eleUnit;

    /**
     *  是否有标识代码表
     */
    @ApiModelProperty("是否有标识代码表")
    private String isCodeParam;

    /**
     *  标识代码表
     */
    @ApiModelProperty("标识代码表")
    private String codeTableId;
}
