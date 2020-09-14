package com.piesat.portal.rpc.dto;

import com.piesat.util.BaseDto;
import lombok.Data;

/**
 * 用户反馈管理
 */
@Data
public class FeedbackManageDto extends BaseDto {
    /**
     * 用户ID
     */
    private String userId;

    /**
     * 反馈人名称
     */
    private String userName;

    /**
     * 反馈内容
     */
    private String content;

    /**
     * 回复内容
     */
    private String reply;


    /**
     * 状态:0未回复 1已回复
     */
    private String status;
}
