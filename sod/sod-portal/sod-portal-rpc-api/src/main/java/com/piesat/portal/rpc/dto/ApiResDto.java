package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * 接口返回值
 */
@Data
public class ApiResDto extends BaseDto {

    /**
     * 接口ID
     */
    private String apiId;

    /**
     * 返回值名称
     */
    private String resName;


    /**
     * 返回值类型
     */
    private String resType;

    /**
     * 返回值描述
     */
    private String resDesc;

    /**
     * 序号
     */
    private String serialNumber;

}
