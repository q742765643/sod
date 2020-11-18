package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * 接口参数
 */
@Data
public class ApiParamDto extends BaseDto {
    /**
     * 接口ID
     */
    private String apiId;

    /**
     * 参数名称
     */
    private String paramName;

    /**
     * 参数类型
     */
    private String paramType;

    /**
     * 参数描述
     */
    private String paramDesc;

    /**
     * 是否必选 Y:必选 N:不是必选
     */
    private String isneed;

    /**
     * 序号
     */
    private String serialNumber;
}
