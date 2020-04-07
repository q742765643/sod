package com.piesat.dm.rpc.dto.dataapply;

import com.piesat.util.BaseDto;
import lombok.Data;

import java.util.Date;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/30 12:20
 */
@Data
public class DataResourceApplyDto extends BaseDto {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 资源类型
     */
    private String srcType;

    /**
     * 资源名称
     */
    private String srcName;

    /**
     * 资源id
     */
    private String srcId;

    /**
     * 审核人
     */
    private String examiner;

    /**
     * 审核状态
     * 01未审核;02通过;03驳回
     */
    private String examineStatus;

    /**
     * 审核时间
     */
    private Date examineTime;

    /**
     * 用途
     */
    private String uses;

    /**
     * 机构编号
     */
    private String orgcode;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 拒绝原因
     */
    private String failureReason;
}
