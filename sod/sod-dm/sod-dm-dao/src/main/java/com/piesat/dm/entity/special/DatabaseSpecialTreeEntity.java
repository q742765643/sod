package com.piesat.dm.entity.special;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/27 18:05
 */
@Data
@Entity
@Table(name = "T_SOD_DATABASE_SPECIAL_TREE")
public class DatabaseSpecialTreeEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 专题库id
     */
    @Column(name = "sdb_id")
    private String sdbId;

    /**
     * 分类id
     */
    @Column(name = "type_id")
    private String typeId;

    /**
     * 分类名称
     */
    @Column(name = "type_name")
    private String typeName;

    /**
     * 上级分类
     */
    @Column(name = "parent_id")
    private String parentId;

    /**
     * 排序
     */
    @Column(name = "sort")
    private Integer sort;

}
