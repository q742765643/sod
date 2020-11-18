package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

@Data
public class TypicalAppDto extends BaseDto {

    /**
     * 类别 : 国家级:C , 省级:P , 公共云:CC
     */
    private String classCode;

    /**
     * 图标路径
     */
    private String icon;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 所属机构
     */
    private String orgName;

    /**
     * 跳转链接
     */
    private String url;

    /**
     * 个数
     */
    private String num;


    /**
     * 序号
     */
    private String serialNumber;

    /**
     * 是否显示
     * 是否显示 : Y 是 , N:否
     */
    private String isshow;

}
