package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.List;

/**
 * 接口管理
 */
@Data
public class ApiManageDto extends BaseDto {

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 接口属于哪个系统
     * 属于哪个系统:sod,cms,music,dpl
     */
    private String apiSys;

    /**
     * 接口分类
     */
    private String apiType;

    /**
     * 调用方法:G:get P:post A:get/post
     */
    private String apiHttptype;


    /**
     * 调用样例
     */
    private String apiExample;

    /**
     * 发布状态:1/发布 , 0/未发布
     */
    private String status;

    /**
     * 备注
     */
    private String remark;

    /**
     * 序号
     */
    private String serialNumber;

    /**
     * 接口描述
     */
    private String apiDesc;

    List<ApiCodeDto> apiCodeDtos;

    List<ApiParamDto> apiParamDtos;

    List<ApiResDto> apiResDtos;

}
