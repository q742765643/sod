package com.piesat.dm.entity.special;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 专题库管理
 *
 * @author wulei
 * @date 2020年 2月12日 15:12:47
 */
@Data
@Table(name = "T_SOD_DATABASE_SPECIAL")
@Entity
//@Proxy(lazy = false)
public class DatabaseSpecialEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name = "序号",width = 10)
    @Transient
    private Integer num;
    /**
     * 专题库名称
     */
    @Column(name = "sdb_name")
    @Excel(name = "专题库名称")
    private String sdbName;

    /**
     * 专题库图片
     */
    @Column(name = "sdb_img")
    private String sdbImg;

    /**
     * 用户ID
     */
    @Column(name = "user_id")
    @Excel(name = "用户ID")
    private String userId;

    /**
     * 用途
     */
    @Column(name = "uses")
    @Excel(name = "用途")
    private String uses;

    /**
     * 申请材料
     */
    @Column(name = "apply_material")
    private String applyMaterial;

    /**
     * 审核人
     */
    @Column(name = "examiner")
    private String examiner;

    /**
     * 审核状态
     */
    @Column(name = "examine_status")
    @Excel(name = "审核状态",readConverterExp = "1=待审,2=已审核,3=审核不通过,4=再次审核")
    private String examineStatus;

    /**
     * 审核时间
     */
    @Column(name = "examine_time")
    @Excel(name = "审核时间",dateFormat = "yyyy-MM-dd HH:mm:ss", type = Excel.Type.EXPORT)
    private Date examineTime;

    /**
     * 拒绝原因
     */
    @Column(name = "failure_reason")
    private String failureReason;

    /**
     * 使用状态
     * 1未使用，2使用中，3废弃
     */
    @Column(name = "use_status")
    private String useStatus;

    /**
     * 数据库ID
     */
    @Column(name = "database_id")
    private String databaseId;

    /**
     * 数据库模式
     */
    @Column(name = "database_schema")
    @Excel(name = "专题库简称")
    private String databaseSchema;

    /**
     * 排序
     */
    @Column(name = "sort_no")
    private String sortNo;

    @Excel(name = "数据库授权情况",width = 100)
    @Transient
    private String authorizationStatus;
}
