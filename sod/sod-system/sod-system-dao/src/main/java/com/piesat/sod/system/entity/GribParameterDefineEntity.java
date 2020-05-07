package com.piesat.sod.system.entity;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 17:22
 */
@Data
@Entity
@Table(name = "T_SOD_GRIB_PARAMETER_DEFINE")
public class GribParameterDefineEntity extends BaseEntity {

    /**
     * 要素存储短名
     */
    @Excel(name = "要素存储短名")
    @Column(name="ele_code_short", length=30)
    private String eleCodeShort;

    /**
     * 学科
     */
    @Excel(name = "学科")
    @Column(name="subject_id")
    private Integer subjectId;

    /**
     * 参数种类
     */
    @Excel(name = "参数种类")
    @Column(name="classify")
    private Integer classify;

    /**
     * 参数编码
     */
    @Excel(name = "参数编码")
    @Column(name="parameter_id")
    private Integer parameterId;

    /**
     * GRIB版本
     */
    @Excel(name = "GRIB版本")
    @Column(name="grib_version")
    private Integer gribVersion;

    /**
     * 中文描述
     */
    @Excel(name = "中文描述")
    @Column(name="element_cn", length=150)
    private String elementCn;

    /**
     * 是否为共有配置
     */
    @Excel(name = "是否为共有配置")
    @Column(name="public_config", length=1)
    private String publicConfig;

    /**
     * 模板编号
     */
    @Excel(name = "模板编号")
    @Column(name="template_id", length=30)
    private String templateId;

    /**
     * 模板说明
     */
    @Excel(name = "模板说明")
    @Column(name="template_desc", length=150)
    private String templateDesc;

}
