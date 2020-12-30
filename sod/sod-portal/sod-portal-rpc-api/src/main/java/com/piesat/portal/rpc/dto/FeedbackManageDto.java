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
     * 处理回复内容
     */
    private String reply;


    /**
     * 状态:0未回复 1已回复
     */
    private String status;

    /**
     * 处理人
     */
    private String checker;

    /**
     * 反馈标题
     */
    private String feedbackTitle;

    /**
     * 反馈内容类型
     */
    private String feedbackType;

    /**
     * 反馈回复时间
     */
    private String replyTime;

    /**
     * 反馈标识（N-个人;Y-公开）
     */
    private String isShow;


}
