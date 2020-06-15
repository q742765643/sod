package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月27日 15:26:37
 */
@Data
@Table(name = "TAB_OMIN_CM_CC_DATUMTYPEINFO")
@Entity
public class DatumTypeInfoEntity{

    private static final long serialVersionUID = 1L;

    /**
     * c_datum_code
     */
    @Id
    @Column(name = "c_datum_code", length = 32, nullable = false)
    private String cDatumCode;

    /**
     * c_datumtype
     */
    @Column(name = "c_datumtype", columnDefinition = "varchar(256)")
    private String cDatumtype;

    /**
     * c_datum_level
     */
    @Column(name = "c_datum_level", length = 32)
    private String cDatumLevel;

    /**
     * c_flow_id
     */
    @Column(name = "c_flow_id", length = 32)
    private String cFlowId;

    /**
     * c_datumparent_code
     */
    @Column(name = "c_datumparent_code", length = 32)
    private String cDatumparentCode;

    /**
     * c_coremetar_name
     */
    @Column(name = "c_coremetar_name", length = 128)
    private String cCoremetarName;

    /**
     * c_sys_code
     */
    @Column(name = "c_sys_code", length = 128)
    private String cSysCode;

    /**
     * c_flow
     */
    @Column(name = "c_flow", length = 128)
    private String cFlow;

    /**
     * c_sys_name
     */
    @Column(name = "c_sys_name", length = 128)
    private String cSysName;

    /**
     * c_opt_type
     */
    @Column(name = "c_opt_type", length = 128)
    private String cOptType;

    /**
     * c_status
     */
    @Column(name = "c_status", length = 32)
    private String cStatus;

    /**
     * c_coremeta_code
     */
    @Column(name = "c_coremeta_code", length = 128)
    private String cCoremetaCode;

    /**
     * c_msg_header
     */
    @Column(name = "c_msg_header", length = 32)
    private String cMsgHeader;

    /**
     * c_datatype
     */
    @Column(name = "c_datatype", length = 32)
    private String cDatatype;

    /**
     * c_traeeflag
     */
    @Column(name = "c_traeeflag", length = 32)
    private String cTraeeflag;

    /**
     * c_datum_process_info
     */
    @Column(name = "c_datum_process_info", length = 32)
    private String cDatumProcessInfo;

    /**
     * c_pri
     */
    @Column(name = "c_pri", length = 50)
    private String cPri;

    /**
     * c_netstation_type
     */
    @Column(name = "c_netstation_type", length = 5)
    private String cNetstationType;

    /**
     * c_is_key_data
     */
    @Column(name = "c_is_key_data", length = 5)
    private String cIsKeyData;

    /**
     * c_access
     */
    @Column(name = "c_access", length = 4)
    private String cAccess;

    /**
     * cci_root_type_code
     */
    @Column(name = "cci_root_type_code", length = 32)
    private String cciRootTypeCode;

    /**
     * c_submit_date
     */
    @Column(name = "c_submit_date")
    private Date cSubmitDate;

    /**
     * c_submit_userid
     */
    @Column(name = "c_submit_userid", length = 32)
    private String cSubmitUserid;

    /**
     * c_submit_username
     */
    @Column(name = "c_submit_username", length = 32)
    private String cSubmitUsername;

    /**
     * c_submit_org_id
     */
    @Column(name = "c_submit_org_id", length = 32)
    private String cSubmitOrgId;

    /**
     * c_submit_org_name
     */
    @Column(name = "c_submit_org_name", length = 64)
    private String cSubmitOrgName;

    /**
     * c_data_level
     */
    @Column(name = "c_data_level", length = 1)
    private String cDataLevel;

    /**
     * c_pubdate
     */
    @Column(name = "c_pubdate")
    private Date cPubdate;

    /**
     * c_datumtypeinfops_id
     */
    @Column(name = "c_datumtypeinfops_id", length = 32)
    private String cDatumtypeinfopsId;

    /**
     * c_rule_bank_id
     */
    @Column(name = "c_rule_bank_id", length = 32)
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
    @Column(name = "c_modifier", length = 36)
    private String cModifier;

    /**
     * versio
     */
    @Column(name = "versio")
    private Integer versio;

    /**
     * c_business_frequency
     */
    @Column(name = "c_business_frequency",columnDefinition = "varchar(3000)")
    private String cBusinessFrequency;

    /**
     * c_nettype
     */
    @Column(name = "c_nettype", length = 8)
    private String cNettype;

    /**
     * c_coremeta_id
     */
    @Column(name = "c_coremeta_id", length = 36)
    private String cCoremetaId;
}
