package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

@Data
public class DataPlateDto extends BaseDto {

    /**
     * 四级编码
     */
    private String dDataId;

    /**
     * 分类:1气象业务数据,2,地球科学数据,3社会行业数据
     */
    private String module;

    /**
     * 数据名称
     */
    private String dataName;

    /**
     * 图标
     */
    private String icon;

    /**
     * 序号
     */
    private Integer serialNumber;

    /**
     * 是否显示
     * 是否显示 : Y 是 , N:否
     */
    private String isshow;
}
