package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * 业务动态管理
 */
@Data
public class DynManageDto extends BaseDto {
    /**
     * 标题
     */
    private String title;

    /**
     * 正文
     */
    private String content;


    /**
     * 动态类型
     */
    private String newsType;


    /**
     * 发布状态
     */
    private String ispublished;

    /**
     * 序号
     */
    private String serialNumber;
}
