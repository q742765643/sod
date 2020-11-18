package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * 接口样例代码
 */
@Data
public class ApiCodeDto extends BaseDto {
    /**
     * 接口ID
     */
    private String apiId;

    /**
     * 示例语言
     */
    private String codeLang;

    /**
     * 调用方式
     * 例如 : rest ,客户端
     */
    private String useType;

    /**
     * 返回值方式
     * 例如 : json , html
     */
    private String resType;

    /**
     * 参数示例
     */
    private String paramCode;

    /**
     * 调用示例
     */
    private String useCode;

    /**
     * 返回值示例
     */
    private String resCode;

    /**
     * 序号
     */
    private String serialNumber;
}
