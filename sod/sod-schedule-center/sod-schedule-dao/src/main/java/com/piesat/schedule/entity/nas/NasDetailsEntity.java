package com.piesat.schedule.entity.nas;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Data
@Entity
@Table(name="T_SOD_NAS_DETAILS")
public class NasDetailsEntity extends BaseEntity {
    @Column(name="user_id", length=50)
    private String userId;
    @Column(name="user_name", length=50)
    private String userName;
    @Column(name="hard_threshold", length=50)
    private BigDecimal hardThreshold;
    @Column(name="soft_threshold", length=50)
    private BigDecimal softThreshold;
    @Column(name="recommended_threshold", length=50)
    private BigDecimal recommendedThreshold;
    @Column(name="private_directory", length=255)
    private String privateDirectory;
    @Column(name="ips", length=255)
    private String  ips;
    @Column(name="nas_vendor", length=50)
    private String nasVendor;
    @Column(name="is_docker", length=2)
    private String isDocker;
    @Column(name="permission", length=10)
    private String permission;
    @Column(name="review_materials", length=255)
    private String reviewMaterials;
    @Column(name="review_materials_pdf", length=255)
    private String reviewMaterialsPdf;
    @Column(name="resource_status", length=2)
    private String resourceStatus;
}
