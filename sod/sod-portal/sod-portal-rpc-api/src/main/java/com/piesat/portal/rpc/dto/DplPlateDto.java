package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

@Data
public class DplPlateDto extends BaseDto {
    /**
     * 分类编号
     */
    private String classifyId;

    /**
     * 算法分类名称
     */
    private String algName;

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
