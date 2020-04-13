package com.piesat.dm.entity.dataapply;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 11:33
 */
@Entity
@Data
@Table(name = "T_SOD_DATA_AUTHORITY_APPLY")
public class DataAuthorityApplyEntity extends BaseEntity {

    /**
     * 申请用户id
     */
    @Column(name = "user_id", length = 50)
    private String userId;

    /**
     * 审核状态
     * 01：待审、02：已审
     */
    @Column(name = "audit_status", length = 2)
    private String auditStatus;

    /**
     * 审核人
     */
    @Column(name = "examiner", length = 30)
    private String examiner;

    /**
     * 审核时间
     */
    @Column(name = "examine_time")
    private Date examineTime;


    @OneToMany(cascade = CascadeType.ALL,  fetch = FetchType.EAGER)
    @JoinColumn(name="apply_id")
    private Set<DataAuthorityRecordEntity> dataAuthorityRecordList = new HashSet<>();

}
