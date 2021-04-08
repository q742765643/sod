package com.piesat.dm.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author cwh
 * @program: sod
 * @description: 审核记录
 * @date 2021年 01月18日 18:13:04
 */
@Data
public class ReviewLogDto extends BaseDto {
    private String bindId;
    /**
     * 材料
     */
    private String document;

    /**
     * 操作用户id
     */
    private String userId;

    /**
     * 状态信息，1-申请，2-通过，3-拒绝，4-修改
     */
    private String statusInfo;

    /**
     * 备注
     */
    private String remark;
}
