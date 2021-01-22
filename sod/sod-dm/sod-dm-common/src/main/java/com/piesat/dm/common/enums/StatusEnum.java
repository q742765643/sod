package com.piesat.dm.common.enums;

import io.swagger.models.auth.In;

/**
 * @author cuiwenhui
 */

public enum StatusEnum {
    待审核(1, "待审核"), 审核通过(2, "审核通过"), 审核未通过(3, "审核未通过"), 删除待审(4, "删除待审"), 已删除(5, "已删除");


    private Integer code;
    /**
     * 状态
     */
    private String status;

    StatusEnum(Integer code, String status) {
        this.code = code;
        this.status = status;
    }

    public static StatusEnum match(Integer code) {
        if (code != null) {
            for (StatusEnum item : StatusEnum.values()) {
                if (item.getCode().equals(code)) {
                    return item;
                }
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
