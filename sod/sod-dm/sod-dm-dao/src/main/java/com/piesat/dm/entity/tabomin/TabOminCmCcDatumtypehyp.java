package com.piesat.dm.entity.tabomin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-15 18:55
 **/
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_DATUMTYPEHYP")
public class TabOminCmCcDatumtypehyp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_datumtypehyp_id
     */
    @Column(name="c_datumtypehyp_id",length = 50)
    private String cDatumtypehypId;

    /**
     * c_datum_code
     */
    @Column(name="c_datum_code",length = 50)
    private String cDatumCode;

    /**
     * c_soursdatum_code
     */
    @Column(name="c_soursdatum_code",length = 50)
    private String cSoursdatumCode;

    /**
     * c_create_date
     */
    @Column(name="c_create_date")
    private Date cCreateDate;

    /**
     * c_create_userid
     */
    @Column(name = "c_create_userid",length = 32)
    private String cCreateUserid;

    /**
     * c_create_username
     */
    @Column(name = "c_create_username",length = 32)
    private String cCreateUsername;

    /**
     * c_create_org_id
     */
    @Column(name = "c_create_org_id",length = 32)
    private String cCreateOrgId;

    /**
     * c_create_org_name
     */
    @Column(name = "c_create_org_name",length = 64)
    private String cCreateOrgName;

    /**
     * c_data_level
     */
    @Column(name = "c_data_level",length = 1)
    private String cDataLevel;

    /**
     * c_data_sources
     */
    @Column(name = "c_data_sources",length = 32)
    private String cDataSources;

    /**
     * c_updated_date
     */
    @Column(name = "c_updated_date")
    private Date cUpdatedDate;

    /**
     * detail_satte
     */
    @Column(name = "detail_satte",length = 32)
    private String detailSatte;

    public TabOminCmCcDatumtypehyp() {
    }

}
