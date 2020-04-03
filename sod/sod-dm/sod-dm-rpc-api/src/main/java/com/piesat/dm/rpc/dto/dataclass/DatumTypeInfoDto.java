package com.piesat.dm.rpc.dto.dataclass;

import lombok.Data;

import java.util.Date;

/**
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月27日 16:21:40
 */
@Data
public class DatumTypeInfoDto {

    private static final long serialVersionUID = 1L;

    private String id;
    /**
     * c_datum_code
     */
    private String cDatumCode;

    /**
     * c_datumtype
     */
    private String cDatumtype;

    /**
     * c_datum_level
     */
    private String cDatumLevel;

    /**
     * c_flow_id
     */
    private String cFlowId;

    /**
     * c_datumparent_code
     */
    private String cDatumparentCode;

    /**
     * c_coremetar_name
     */
    private String cCoremetarName;

    /**
     * c_sys_code
     */
    private String cSysCode;

    /**
     * c_flow
     */
    private String cFlow;

    /**
     * c_sys_name
     */
    private String cSysName;

    /**
     * c_opt_type
     */
    private String cOptType;

    /**
     * c_status
     */
    private String cStatus;

    /**
     * c_coremeta_code
     */
    private String cCoremetaCode;

    /**
     * c_msg_header
     */
    private String cMsgHeader;

    /**
     * c_datatype
     */
    private String cDatatype;

    /**
     * c_traeeflag
     */
    private String cTraeeflag;

    /**
     * c_datum_process_info
     */
    private String cDatumProcessInfo;

    /**
     * c_pri
     */
    private String cPri;

    /**
     * c_netstation_type
     */
    private String cNetstationType;

    /**
     * c_is_key_data
     */
    private String cIsKeyData;

    /**
     * c_access
     */
    private String cAccess;

    /**
     * cci_root_type_code
     */
    private String cciRootTypeCode;

    /**
     * c_submit_date
     */
    private Date cSubmitDate;

    /**
     * c_submit_userid
     */
    private String cSubmitUserid;

    /**
     * c_submit_username
     */
    private String cSubmitUsername;

    /**
     * c_submit_org_id
     */
    private String cSubmitOrgId;

    /**
     * c_submit_org_name
     */
    private String cSubmitOrgName;

    /**
     * c_data_level
     */
    private String cDataLevel;

    /**
     * c_pubdate
     */
    private Date cPubdate;

    /**
     * c_datumtypeinfops_id
     */
    private String cDatumtypeinfopsId;

    /**
     * c_rule_bank_id
     */
    private String cRuleBankId;

    /**
     * c_create_date
     */
    private Date cCreateDate;

    /**
     * c_updated_date
     */
    private Date cUpdatedDate;

    /**
     * c_modifier
     */
    private String cModifier;

    /**
     * versio
     */
    private Integer versio;

    /**
     * c_business_frequency
     */
    private String cBusinessFrequency;

    /**
     * c_nettype
     */
    private String cNettype;

    /**
     * c_coremeta_id
     */
    private String cCoremetaId;

    private Date createTime;

    private Date updateTime;

    /**
     * 删除标志（0代表存在 2代表删除）
     */
    private String delFlag;

    private Integer version;
}
