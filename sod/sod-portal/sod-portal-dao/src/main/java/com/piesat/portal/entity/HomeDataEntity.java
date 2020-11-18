package com.piesat.portal.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 首页数据管理
 */
@Data
@Table(name = "T_SOD_PORTAL_HOME_DATA")
@Entity
public class HomeDataEntity extends BaseEntity {

    /**
     * 数据编号
     */
    @Column(name="DATA_CODE")
    private String dataCode;

    /**
     * 数据值
     */
    @Column(name="DATA_VALUE")
    private String dataValue;

    /**
     * 备注
     */
    @Column(name="REMARK")
    private String remark;
}
