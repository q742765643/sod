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
    @Column(name="private_directory", length=255)
    private String privateDirectory;
    @Column(name="ip", length=255)
    private String  ip;
    @Column(name="is_docker", length=2)
    private String isDocker;
    @Column(name="acl_status", length=2)
    private String aclStatus;
}
