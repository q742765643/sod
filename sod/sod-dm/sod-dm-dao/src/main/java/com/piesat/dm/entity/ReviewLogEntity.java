package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author cwh
 * @program: sod
 * @description: 审核日志
 * @date 2021年 01月18日 18:00:57
 */
@Data
@Table(name = "T_SOD_REVIEW_LOG")
@Entity
public class ReviewLogEntity extends BaseEntity {
    /**
     * 绑定ID
     */
    @Column(name = "bind_id", length = 50)
    private String bindId;
    /**
     * 材料
     */
    @Column(name = "document", length = 200)
    private String document;

    /**
     * 操作用户id
     */
    @Column(name = "user_id", length = 50)
    private String userId;

    /**
     * 状态信息
     */
    @Column(name = "status_info")
    private String statusInfo;

    /**
     * 备注
     */
    @Column(name = "remark")
    private String remark;
}
