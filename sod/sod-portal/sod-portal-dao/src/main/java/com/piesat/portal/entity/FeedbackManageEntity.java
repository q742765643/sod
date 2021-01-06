package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 用户反馈管理
 */
@Entity
@Data
@Table(name = "T_SOD_PORTAL_FEEDBACK_MANAGE")
public class FeedbackManageEntity extends BaseEntity {

    /**
     * 反馈人ID
     */
    @Column(name="USER_ID")
    private String userId;

    /**
     * 反馈人名称
     */
    @Column(name="USER_NAME")
    private String userName;

    /**
     * 处理人
     */
    @Column(name="CHECKER")
    private String checker;

    /**
     * 反馈内容
     */
    @Column(name="CONTENT",columnDefinition="TEXT",nullable=true)
    private String content;

    /**
     * 处理回复内容
     */
    @Column(name="REPLY",columnDefinition="TEXT",nullable=true)
    private String reply;

    /**
     * 状态:0未回复 1已回复
     */
    @Column(name="STATUS")
    private String status;

    /**
     * 反馈标题
     */
    @Column(name="FEEDBACK_TITLE")
    private String feedbackTitle;

    /**
     * 反馈内容类型
     */
    @Column(name="FEEDBACK_TYPE")
    private String feedbackType;

    /**
     * 反馈回复时间
     */
    @Column(name="REPLY_TIME")
    private String replyTime;

    /**
     * 反馈标识（N-个人;Y-公开）
     */
    @Column(name="ISSHOW")
    private String isShow;

}
