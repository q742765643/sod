package com.piesat.dm.entity.dataclass;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 资料申请
 *
 * @author cwh
 * @date 2019年 11月20日 17:02:58
 */
@Data
@Table(name = "T_SOD_DATA_CLASS_APPLY")
@Entity
public class DataClassApplyEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 四级编码
     */
    @Column(name = "d_data_id")
    private String dDataId;

    /**
     * 存储编码
     */
    @Column(name = "data_class_id", nullable = false)
    private String dataClassId;


    /**
     * 父节点id
     */
    @Column(name = "parent_id")
    private String parentId;


    /**
     * 序号
     */
    @Column(name = "serial")
    private Integer serial;

    /**
     * 名称
     */
    @Column(name = "class_name")
    private String className;

    /**
     * 绑定表id
     */
    @Column(name = "table_id")
    private String tableId;
    /**
     * 表英文名称
     */
    @Column(name = "table_name", length = 64)
    private String tableName;


    /**
     * 数据库ID
     */
    @Column(name = "database_id", length = 64)
    private String databaseId;

    /**
     * 表描述
     */
    @Column(name = "table_desc", length = 64)
    private String tableDesc;

    /**
     * 是否公开
     */
    @Column(name = "is_access", columnDefinition = "integer DEFAULT 1 ")
    private Integer isAccess;


    /**
     * 审核状态
     * 1-待审核 ,2-审核通过 ,3-审核不通过，4-删除申请中，5-已删除
     */
    @Column(name = "status")
    private Integer status;

    /**
     * 申请人
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 审核人
     */
    @Column(name = "reviewer")
    private String reviewer;

    /**
     * 审核信息
     */
    @Column(name = "review_notes", columnDefinition = "TEXT")
    private String reviewNotes;

    /**
     * 审核时间
     */
    @Column(name = "reviewTime")
    private Date reviewTime;

    /**
     * 申请备注
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;
}
