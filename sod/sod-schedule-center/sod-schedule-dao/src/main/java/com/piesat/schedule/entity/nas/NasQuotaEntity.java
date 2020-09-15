package com.piesat.schedule.entity.nas;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName : NasQuotaEntity
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-14 17:31
 */
@Data
@Entity
@Table(name="T_SOD_NAS_QUOTA")
public class NasQuotaEntity  extends BaseEntity {
    @Column(name="private_directory", length=255)
    private String privateDirectory;
    @Column(name="nas_vendor", length=50)
    private String nasVendor;
    @Column(name="hard_threshold", length=50)
    private BigDecimal hardThreshold;
    @Column(name="review_materials", length=255)
    private String reviewMaterials;
    @Column(name="review_materials_pdf", length=255)
    private String reviewMaterialsPdf;
    @Column(name="audit_results", length=255)
    private String auditResults;
    @Column(name="audit_time", length=255)
    private Date auditTime;
    @Column(name="resource_status", length=2)
    private String resourceStatus;
    @Column(name="nas_id", length=2)
    private String nasId;
}

