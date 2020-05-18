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
 *  tab_omin_cm_cc_idpoc
 * @author 大狼狗 2020-05-18
 */
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_IDPOC")
public class TabOminCmCcIdpoc implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_idpoc_id
     */
    @Column(name="c_idpoc_id",length = 36)
    private String cIdpocId;

    /**
     * c_rpindname
     */
    @Column(name="c_rpindname",length = 300)
    private String cRpindname;

    /**
     * c_unittype
     */
    @Column(name="c_unittype",length = 64)
    private String cUnittype;

    /**
     * c_rporgname
     */
    @Column(name="c_rporgname",length = 600)
    private String cRporgname;

    /**
     * c_rpposname
     */
    @Column(name="c_rpposname",length = 50)
    private String cRpposname;

    /**
     * c_role
     */
    @Column(name="c_role",length = 3)
    private String cRole;

    /**
     * c_cntphone
     */
    @Column(name="c_cntphone",length = 50)
    private String cCntphone;

    /**
     * c_faxphone
     */
    @Column(name="c_faxphone",length = 50)
    private String cFaxphone;

    /**
     * c_emailadd
     */
    @Column(name="c_emailadd",length = 100)
    private String cEmailadd;

    /**
     * c_delpoint
     */
    @Column(name="c_delpoint",length = 600)
    private String cDelpoint;

    /**
     * c_city
     */
    @Column(name="c_city",length = 30)
    private String cCity;

    /**
     * c_adminarea
     */
    @Column(name="c_adminarea",length = 20)
    private String cAdminarea;

    /**
     * c_country
     */
    @Column(name="c_country",length = 30)
    private String cCountry;

    /**
     * c_postcode
     */
    @Column(name="c_postcode",length = 6)
    private String cPostcode;

    /**
     * c_cntonlineres
     */
    @Column(name="c_cntonlineres",length = 100)
    private String cCntonlineres;

    /**
     * c_coremeta_id
     */
    @Column(name="c_coremeta_id",length = 36)
    private String cCoremetaId;

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
     * version
     */
    @Column(name="version")
    private Integer version;

    /**
     * c_opt_type
     */
    @Column(name="c_opt_type",length = 128)
    private String cOptType;

    public TabOminCmCcIdpoc() {
    }

}
