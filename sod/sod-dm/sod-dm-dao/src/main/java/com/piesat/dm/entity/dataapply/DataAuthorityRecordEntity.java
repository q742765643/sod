package com.piesat.dm.entity.dataapply;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 14:43
 */
@Entity
@Data
@Table(name = "T_SOD_DATA_AUTHORITY_RECORD")
public class DataAuthorityRecordEntity extends BaseEntity {

    /**
     * 申请id
     */
    @Column(name = "apply_id", length = 50)
    private String applyId;

    /**
     * 存储编码
     */
    @Column(name = "data_class_id", length = 30)
    private String dataClassId;

    /**
     * 物理库id
     */
    @Column(name = "database_id", length = 50)
    private String databaseId;

    /**
     * 申请权限
     * 1:读权限 2：写权限
     */
    @Column(name = "apply_authority", length = 1)
    private Integer applyAuthority;

    /**
     * 授权
     * 1:允许 2：禁止
     */
    @Column(name = "authorize", length = 1)
    private Integer authorize;

    /**
     * 拒绝原因
     * 当授权为2时需填写拒绝原因
     */
    @Column(name = "cause", length = 50)
    private String cause;

    /**
     * 引用id
     * 专题库引用资料记录引用专题库id
     */
    @Column(name = "qtdb_id", length = 50)
    private String qtdbId;

}
