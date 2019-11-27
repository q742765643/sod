package com.piesat.common.jpa.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-11-17 18:10
 **/
@Data
@SuppressWarnings("serial")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity extends UUIDEntity {

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    @Column(nullable = false)
    private Date createTime;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updateTime;


    /**
     * 创建者
     */
    @Column(name="create_by",columnDefinition = "varchar(64) default ''")
    private String createBy;

    /**
     * 更新者
     */
    @Column(name="update_by",columnDefinition = "varchar(64) default ''")
    private String updateBy;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    @Column(name="DEL_FLAG",columnDefinition = "varchar(1) default '0'")
    private String delFlag;

    @Version
    private Integer version;




}