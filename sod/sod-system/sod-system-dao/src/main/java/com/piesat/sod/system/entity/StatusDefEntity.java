package com.piesat.sod.system.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author cwh
 * @program: sod
 * @description: 审核状态定义
 * @date 2021年 03月09日 11:29:10
 */
@Data
@Entity
@Table(name = "T_SOD_STATUS_DEF")
public class StatusDefEntity extends BaseEntity {

    @Column(name = "status")
    private Integer status;

    @Column(name = "status_desc")
    private String statusDesc;

    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @Column(name = "visiable", columnDefinition = "Boolean default true")
    private Boolean visiable;

    @Column(name = "svr_id")
    private Integer svrId;

}
