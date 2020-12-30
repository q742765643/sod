package com.piesat.dm.entity.dataapply;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Data
@Table(name = "T_SOD_YUN_DATABASE_FEEDBACK_APPLY")
public class YunDatabaseApplyFeedbackEntity extends BaseEntity {

    /**
     * 实例信息id
     */
    @Column(name="feedback_id" ,length=50)
    private String feedbackId;

    /**
     * 实例id
     */
    @Column(name="itservice_id" ,length=20)
    private String itserviceId;
    /**
     * 实例类型
     */
    @Column(name = "storage_logic", length = 20)
    private String storageLogic;
    /**
     * 反馈材料
     */
    @Column(name = "material",columnDefinition="CLOB")
    private String material;
    /**
     * 处理材料
     */
    @Column(name = "handle_material",columnDefinition="CLOB")
    private String handleMaterial;

    /**
     * 标题
     */
    @Column(name = "feedback_title", length = 200)
    private String feedbackTitle;

    /**
     * 反馈处理状态
     */
    @Column(name = "feedback_status", length = 20)
    private String feedbackStatus;
    /**
     * 实例名称
     */
    @Column(name="displayname" ,length=50)
    private String displayname;
    /**
     * 反馈用户id
     */
    @Column(name = "user_id", length = 50)
    private String userId;
    /**
     * 处理人
     */
    @Column(name = "processor", length = 50)
    private String processor;
}
