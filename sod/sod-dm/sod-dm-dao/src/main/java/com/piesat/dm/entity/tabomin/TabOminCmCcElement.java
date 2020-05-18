package com.piesat.dm.entity.tabomin;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.GeneratedValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 *  tab_omin_cm_cc_element
 * @author 大狼狗 2020-05-18
 */
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_ELEMENT")
public class TabOminCmCcElement implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_element_code
     */
    @Column(name="c_element_code",length =50 )
    private String cElementCode;

    /**
     * cci_root_type_code
     */
    @Column(name="cci_root_type_code",length =32)
    private String cciRootTypeCode;

    /**
     * c_element_code_old
     */
    @Column(name="c_element_code_old",length =50)
    private String cElementCodeOld;

    /**
     * c_class_no
     */
    @Column(name="c_class_no",length =50)
    private String cClassNo;

    /**
     * c_element_namech
     */
    @Column(name="c_element_namech",length = 200)
    private String cElementNamech;

    /**
     * c_element_namech_old
     */
    @Column(name="c_element_namech_old",length = 200)
    private String cElementNamechOld;

    /**
     * c_element_name
     */
    @Column(name="c_element_name",length = 200)
    private String cElementName;

    /**
     * c_short_name
     */
    @Column(name="c_short_name",length = 128)
    private String cShortName;

    /**
     * c_short_name_old
     */
    @Column(name="c_short_name_old",length = 128)
    private String cShortNameOld;

    /**
     * c_define
     */
    @Column(name="c_define",length = 128)
    private String cDefine;

    /**
     * c_subject_type
     */
    @Column(name="c_subject_type",length = 200)
    private String cSubjectType;

    /**
     * c_paramter_type
     */
    @Column(name="c_paramter_type",length = 32)
    private String cParamterType;

    /**
     * c_paramter_num
     */
    @Column(name="c_paramter_num",length = 32)
    private String cParamterNum;

    /**
     * c_datatype
     */
    @Column(name="c_datatype",length = 32)
    private String cDatatype;

    /**
     * c_element_unit
     */
    @Column(name="c_element_unit",length = 50)
    private String cElementUnit;

    /**
     * c_element_pre
     */
    @Column(name="c_element_pre",length = 100)
    private String cElementPre;

    /**
     * c_charactervalue
     */
    @Column(name="c_charactervalue",columnDefinition = "TEXT")
    private String cCharactervalue;

    /**
     * c_status
     */
    @Column(name="c_status",length = 32)
    private String cStatus;

    /**
     * c_version
     */
    @Column(name="c_version",length = 32)
    private String cVersion;

    /**
     * c_notes
     */
    @Column(name="c_notes",length = 256)
    private String cNotes;

    /**
     * c_codetable
     */
    @Column(name="c_codetable",length = 50)
    private String cCodetable;

    /**
     * c_submit_org_name
     */
    @Column(name="c_submit_org_name",length = 64)
    private String cSubmitOrgName;

    /**
     * c_create_date
     */
    @Column(name="c_create_date")
    private Date cCreateDate;

    /**
     * c_updated_date
     */
    @Column(name="c_updated_date")
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

    public TabOminCmCcElement() {
    }

}


