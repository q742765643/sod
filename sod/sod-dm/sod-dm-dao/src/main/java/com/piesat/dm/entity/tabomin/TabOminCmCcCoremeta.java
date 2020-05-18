package com.piesat.dm.entity.tabomin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-15 18:36
 **/
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_COREMETA")
public class TabOminCmCcCoremeta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_coremeta_id
     */
    @Column(name="c_coremeta_id",length = 36)
    private String cCoremetaId;

    /**
     * c_mdfileid
     */
    @Column(name = "c_mdfileid",length = 100)
    private String cMdfileid;

    /**
     * c_mdlang
     */
    @Column(name="c_mdlang",length = 36)
    private String cMdlang;

    /**
     * c_mdchar
     */
    @Column(name = "c_mdchar",length = 36)
    private String cMdchar;

    /**
     * c_mddatest
     */
    @Column(name = "c_mddatest",length = 8)
    private String cMddatest;

    /**
     * c_mdstanname
     */
    @Column(name = "c_mdstanname",length = 150)
    private String cMdstanname;

    /**
     * c_mdstanver
     */
    @Column(name = "c_mdstanver",length = 20)
    private String cMdstanver;

    /**
     * c_rpindname
     */
    @Column(name = "c_rpindname",length = 300)
    private String cRpindname;

    /**
     * c_rporgname
     */
    @Column(name="c_rporgname",length = 50)
    private String cRporgname;

    /**
     * c_rpposname
     */
    @Column(name="c_rpposname",length = 50)
    private String cRpposname;

    /**
     * c_role
     */
    @Column(name="c_role",length = 50)
    private String cRole;

    /**
     * c_cntphone
     */
    @Column(name = "c_cntphone",length = 50)
    private String cCntphone;

    /**
     * c_faxphone
     */
    @Column(name="c_faxphone",length = 50)
    private String cFaxphone;

    /**
     * c_delpoint
     */
    @Column(name="c_delpoint",length = 600)
    private String cDelpoint;

    /**
     * c_city
     */
    @Column(name="c_city",length = 36)
    private String cCity;

    /**
     * c_adminarea
     */
    @Column(name="c_adminarea",length = 36)
    private String cAdminarea;

    /**
     * c_postcode
     */
    @Column(name="c_postcode",length = 6)
    private String cPostcode;

    /**
     * c_country
     */
    @Column(name="c_country",length = 36)
    private String cCountry;

    /**
     * c_emailadd
     */
    @Column(name="c_emailadd",length = 100)
    private String cEmailadd;

    /**
     * c_cntonlineres
     */
    @Column(name="c_cntonlineres",columnDefinition = "TEXT")
    private String cCntonlineres;

    /**
     * c_title
     */
    @Column(name="c_title",length = 150)
    private String cTitle;

    /**
     * c_dsid
     */
    @Column(name="c_dsid",length = 100)
    private String cDsid;

    /**
     * c_idabs
     */
    @Column(name = "c_idabs",columnDefinition = "TEXT")
    private String cIdabs;

    /**
     * c_tpcat
     */
    @Column(name="c_tpcat",length = 256)
    private String cTpcat;

    /**
     * c_datalang
     */
    @Column(name = "c_datalang",length = 32)
    private String cDatalang;

    /**
     * c_datachar
     */
    @Column(name="c_datachar",length = 32)
    private String cDatachar;

    /**
     * c_mainfreq
     */
    @Column(name="c_mainfreq",length = 32)
    private String cMainfreq;

    /**
     * c_datascal
     */
    @Column(name="c_datascal",columnDefinition = "TEXT")
    private String cDatascal;

    /**
     * c_statement
     */
    @Column(name="c_statement",columnDefinition = "TEXT")
    private String cStatement;

    /**
     * c_lineage
     */
    @Column(name="c_lineage",columnDefinition = "TEXT")
    private String cLineage;

    /**
     * c_source
     */
    @Column(name="c_source",columnDefinition = "TEXT")
    private String cSource;

    /**
     * c_refsystem
     */
    @Column(name="c_refsystem",length = 200 )
    private String cRefsystem;

    /**
     * c_status
     */
    @Column(name="c_status",length = 32)
    private String cStatus;

    /**
     * c_data_level
     */
    @Column(name="c_data_level",length = 1)
    private String cDataLevel;

    /**
     * c_pubdate
     */
    @Column(name="c_pubdate")
    private Date cPubdate;

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
    @Column(name="c_updated_date")
    private Date cUpdatedDate;

    /**
     * c_attribute
     */
    @Column(name="c_attribute",columnDefinition = "TEXT")
    private String cAttribute;

    /**
     * c_industry
     */
    @Column(name="c_industry",columnDefinition = "TEXT")
    private String cIndustry;

    /**
     * c_data_increment
     */
    @Column(name="c_data_increment",columnDefinition = "TEXT")
    private String cDataIncrement;

    /**
     * c_data_time
     */
    @Column(name="c_data_time",columnDefinition = "TEXT")
    private String cDataTime;

    /**
     * c_data_storage_mode
     */
    @Column(name="c_data_storage_mode",columnDefinition = "TEXT")
    private String cDataStorageMode;

    /**
     * c_issue_number
     */
    @Column(name="c_issue_number",columnDefinition = "TEXT")
    private String cIssueNumber;

    /**
     * c_cover_photo
     */
    @Column(name="c_cover_photo",columnDefinition = "TEXT")
    private String cCoverPhoto;

    /**
     * c_back_cover_photo
     */
    @Column(name="c_back_cover_photo",columnDefinition = "TEXT")
    private String cBackCoverPhoto;

    /**
     * c_application_stutas
     */
    @Column(name="c_application_stutas",columnDefinition = "TEXT")
    private String cApplicationStutas;

    /**
     * c_medname
     */
    @Column(name="c_medname",length = 3)
    private String cMedname;

    /**
     * version
     */
    @Column(name = "version")
    private Integer version;

    /**
     * c_short_name
     */
    @Column(name = "c_short_name",length = 36)
    private String cShortName;

    public TabOminCmCcCoremeta() {
    }
}

