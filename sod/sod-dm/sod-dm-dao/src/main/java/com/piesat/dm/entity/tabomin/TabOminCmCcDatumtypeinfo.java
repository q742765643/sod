package com.piesat.dm.entity.tabomin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-15 18:59
 **/
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_DATUMTYPEINFO")
public class TabOminCmCcDatumtypeinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_datum_code
     */
    @Column(name = "c_datum_code",length = 32)
    private String cDatumCode;

    /**
     * c_datumtype
     */
    @Column(name = "c_datumtype",length = 256)
    private String cDatumtype;

    /**
     * c_datum_level
     */
    @Column(name = "c_datum_level",length = 32)
    private String cDatumLevel;

    /**
     * c_flow_id
     */
    @Column(name = "c_flow_id",length = 32)
    private String cFlowId;

    /**
     * c_datumparent_code
     */
    @Column(name = "c_datumparent_code",length = 32)
    private String cDatumparentCode;

    /**
     * c_coremetar_name
     */
    @Column(name = "c_coremetar_name",length = 128)
    private String cCoremetarName;

    /**
     * c_sys_code
     */
    @Column(name = "c_sys_code",length = 128)
    private String cSysCode;

    /**
     * c_flow
     */
    @Column(name = "c_flow",length = 128)
    private String cFlow;

    /**
     * c_sys_name
     */
    @Column(name = "c_sys_name",length = 128)
    private String cSysName;

    /**
     * c_opt_type
     */
    @Column(name = "c_opt_type",length = 128)
    private String cOptType;

    /**
     * c_status
     */
    @Column(name = "c_status",length = 32)
    private String cStatus;

    /**
     * c_coremeta_code
     */
    @Column(name = "c_coremeta_code",length = 128)
    private String cCoremetaCode;

    /**
     * c_msg_header
     */
    @Column(name = "c_msg_header",length = 32)
    private String cMsgHeader;

    /**
     * c_datatype
     */
    @Column(name = "c_datatype",length = 32)
    private String cDatatype;

    /**
     * c_traeeflag
     */
    @Column(name = "c_traeeflag",length = 32)
    private String cTraeeflag;

    /**
     * c_datum_process_info
     */
    @Column(name = "c_datum_process_info",length = 32)
    private String cDatumProcessInfo;

    /**
     * c_pri
     */
    @Column(name = "c_pri",length = 50)
    private String cPri;

    /**
     * c_netstation_type
     */
    @Column(name = "c_netstation_type",length = 5)
    private String cNetstationType;

    /**
     * c_is_key_data
     */
    @Column(name = "c_is_key_data",length = 5)
    private String cIsKeyData;

    /**
     * c_access
     */
    @Column(name = "c_access",length = 4)
    private String cAccess;

    /**
     * cci_root_type_code
     */
    @Column(name = "cci_root_type_code",length = 32)
    private String cciRootTypeCode;

    /**
     * c_submit_date
     */
    @Column(name = "c_submit_date")
    private Date cSubmitDate;

    /**
     * c_submit_userid
     */
    @Column(name = "c_submit_userid",length = 32)
    private String cSubmitUserid;

    /**
     * c_submit_username
     */
    @Column(name = "c_submit_username",length = 32)
    private String cSubmitUsername;

    /**
     * c_submit_org_id
     */
    @Column(name = "c_submit_org_id",length = 32)
    private String cSubmitOrgId;

    /**
     * c_submit_org_name
     */
    @Column(name = "c_submit_org_name",length = 64)
    private String cSubmitOrgName;

    /**
     * c_data_level
     */
    @Column(name = "c_data_level",length = 1)
    private String cDataLevel;

    /**
     * c_pubdate
     */
    @Column(name = "c_pubdate")
    private Date cPubdate;

    /**
     * c_datumtypeinfops_id
     */
    @Column(name = "c_datumtypeinfops_id",length = 32)
    private String cDatumtypeinfopsId;

    /**
     * c_rule_bank_id
     */
    @Column(name = "c_rule_bank_id",length = 32)
    private String cRuleBankId;

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
     * version
     */
    @Column(name = "version")
    private Integer version;

    /**
     * c_business_frequency
     */
    @Column(name = "c_business_frequency",length = 3000)
    private String cBusinessFrequency;

    /**
     * c_nettype
     */
    @Column(name = "c_nettype",length = 8)
    private String cNettype;

    /**
     * c_coremeta_id
     */
    @Column(name = "c_coremeta_id",length = 36)
    private String cCoremetaId;

    public TabOminCmCcDatumtypeinfo() {
    }

}


