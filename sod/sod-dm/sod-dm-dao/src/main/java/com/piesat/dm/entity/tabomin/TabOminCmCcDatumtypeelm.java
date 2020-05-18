package com.piesat.dm.entity.tabomin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-15 18:49
 **/
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_DATUMTYPEELM")
public class TabOminCmCcDatumtypeelm implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_datumtypeelm_id
     */
    @Column(name="c_datumtypeelm_id",length = 36)
    private String cDatumtypeelmId;

    /**
     * c_datum_code
     */
    @Column(name="c_datum_code",length = 50)
    private String cDatumCode;

    /**
     * c_element_code
     */
    @Column(name = "c_element_code",length = 36)
    private String cElementCode;

    /**
     * c_isqc
     */
    @Column(name="c_isqc",length = 32)
    private String cIsqc;

    /**
     * c_status
     */
    @Column(name="c_status",length = 32)
    private String cStatus;

    /**
     * c_no
     */
    @Column(name="c_no",length = 4)
    private String cNo;

    /**
     * c_data_level
     */
    @Column(name="c_data_level",length = 1)
    private String cDataLevel;

    /**
     * c_submit_org_name
     */
    @Column(name="c_submit_org_name",length = 64)
    private String cSubmitOrgName;

    /**
     * c_submit_org_id
     */
    @Column(name="c_submit_org_id",length = 32)
    private String cSubmitOrgId;

    /**
     * c_submit_date
     */
    @Column(name="c_submit_date")
    private Date cSubmitDate;

    /**
     * c_submit_username
     */
    @Column(name="c_submit_username",length = 32)
    private String cSubmitUsername;

    /**
     * c_submit_userid
     */
    @Column(name="c_submit_userid",length = 32)
    private String cSubmitUserid;

    /**
     * c_pubdate
     */
    @Column(name = "c_pubdate")
    private Date cPubdate;

    /**
     * c_flow_id
     */
    @Column(name="c_flow_id",length = 32)
    private String cFlowId;

    /**
     * c_opt_type
     */
    @Column(name="c_opt_type",length = 32)
    private String cOptType;

    /**
     * c_create_date
     */
    @Column(name="c_create_date")
    private Date cCreateDate;

    /**
     * c_updated_date
     */
    @Column(name = "c_updated_date")
    private Date cUpdatedDate;

    /**
     * c_modifier
     */
    @Column(name="c_modifier",length = 36)
    private String cModifier;

    /**
     * version
     */
    @Column(name="version")
    private Integer version;

    public TabOminCmCcDatumtypeelm() {
    }

}
