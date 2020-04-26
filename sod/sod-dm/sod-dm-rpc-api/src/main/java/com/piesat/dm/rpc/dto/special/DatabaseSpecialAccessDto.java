package com.piesat.dm.rpc.dto.special;

import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/25 22:06
 */
@Data
public class DatabaseSpecialAccessDto extends BaseDto {

    /**
     * 专题库ID
     */
    private String sdbId;

    /**
     * 访问权限
     *  1：读权限
     *  2：完整访问权限
     */
    private Integer accessAuthority;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用途
     */
    private String uses;

    /**
     * 审核人
     */
    private String examiner;

    /**
     * 审核状态
     * 1：待审核
     * 2：审核通过
     * 3：审核不通过
     */
    private String examineStatus;

    /**
     * 审核时间
     */
    private Date examineTime;

    /**
     * 拒绝原因
     */
    private String failureReason;

    /**
     * 使用状态
     * 1：未使用
     * 2：使用中
     * 3：撤销使用
     */
    private String useStatus;
}
