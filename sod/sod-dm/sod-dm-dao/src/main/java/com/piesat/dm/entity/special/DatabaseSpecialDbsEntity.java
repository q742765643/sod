package com.piesat.dm.entity.special;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author cwh
 * @version 1.0.0
 * @ClassName DatabaseSpecialDbsEntity.java
 * @Description TODO
 * @createTime 2021年04月04日 14:11:00
 */
@Data
@Table(name = "T_SOD_DATABASE_SPECIAL_DBS")
@Entity
public class DatabaseSpecialDbsEntity extends BaseEntity   {

    /**
     * 已有空间(GB)
     */
    @Excel(name = "已有空间")
    @Column(name = "size_of_space")
    private Integer sizeOfSpace;

    /**
     * 申请空间(GB)
     */
    @Excel(name = "申请空间")
    @Column(name = "apply_of_space")
    private Integer applyOfSpace;

    /**
     * 数据库ID
     */
    @Column(name = "database_id")
    private String databaseId;

    /**
     * 专题库类型(DB,NAS两种)
     */
    @Column(name = "db_type")
    private String dbType;

    /**
     * NAS路径
     */
    @Column(name = "nas_route")
    private String nasRoute;
}
