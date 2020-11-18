package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * SDK管理
 */
@Data
public class SdkManageDto extends BaseDto {
    /**
     * sdk类型
     */
    private String sdkType;

    /**
     * 开发语言
     */
    private String sdkLang;

    /**
     * 操作系统
     */
    private String sdkSys;

    /**
     * sdk包名称
     */
    private String sdkJarName;

    /**
     * sdk包地址
     */
    private String sdkJarUrl;

    /**
     * sdk文档名称
     */
    private String sdkDocName;

    /**
     * sdk文档地址
     */
    private String sdkDocUrl;

    /**
     * sdk描述
     */
    private String sdkDesc;

    /**
     * 序号
     */
    private String serialNumber;
}
