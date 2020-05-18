package com.piesat.dm.entity.tabomin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-15 18:29
 **/
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_APPLY")
public class TabOminCmCcApply implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_apply_id
     */
    @Column(name = "c_apply_id" ,length = 36)
    private String cApplyId;

    /**
     * c_status
     */
    @Column(name = "c_status" ,length = 36)
    private String cStatus;

    /**
     * c_flow_type
     */
    @Column(name = "c_flow_type" ,length = 8)
    private String cFlowType;

    /**
     * c_data_id
     */
    @Column(name = "c_data_id" ,length = 128)
    private String cDataId;

    /**
     * c_apply_type
     */
    @Column(name = "c_apply_type" ,length = 8)
    private String cApplyType;

    /**
     * c_version
     */
    @Column(name = "c_version",length = 8)
    private String cVersion;

    /**
     * c_version_memo
     */
    @Column(name = "c_version_memo",length = 128)
    private String cVersionMemo;

    /**
     * c_creat_id
     */
    @Column(name = "c_creat_id",length = 36)
    private String cCreatId;

    /**
     * c_creat_name
     */
    @Column(name = "c_creat_name",length = 36)
    private String cCreatName;

    /**
     * c_creat_org_id
     */
    @Column(name = "c_creat_org_id",length = 36)
    private String cCreatOrgId;

    /**
     * c_submit_date
     */
    @Column(name = "c_submit_date",length = 16)
    private String cSubmitDate;

    /**
     * c_pubdate
     */
    @Column(name = "c_pubdate",length = 16)
    private String cPubdate;

    /**
     * c_create_date
     */
    @Column(name = "c_create_date")
    private Date cCreateDate;

    /**
     * c_updated_date
     */
    @Column(name = "c_updated_date")
    private Date cUpdatedDate;

    /**
     * c_modifier
     */
    @Column(name = "c_modifier",length = 36)
    private String cModifier;

    /**
     * c_apply_num
     */
    @Column(name = "c_apply_num",length = 36)
    private String cApplyNum;

    /**
     * c_checker_id
     */
    @Column(name = "c_checker_id",length = 36)
    private String cCheckerId;

    /**
     * c_checker
     */
    @Column(name = "c_checker",length = 36)
    private String cChecker;

    /**
     * c_check_time
     */
    @Column(name = "c_check_time",length = 16)
    private String cCheckTime;

    /**
     * c_pubper
     */
    @Column(name = "c_pubper",length = 36)
    private String cPubper;

    public TabOminCmCcApply() {
    }

}
