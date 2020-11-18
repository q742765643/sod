package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * 首页数据管理
 */
@Data
public class HomeDataDto extends BaseDto {

    /**
     * 数据编号
     */
    private String dataCode;

    /**
     * 数据值
     */
    private String dataValue;

    /**
     * 备注
     */
    private String remark;

}
