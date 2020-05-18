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
 *  tab_omin_cm_cc_geoele
 * @author 大狼狗 2020-05-18
 */
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_GEOELE")
public class TabOminCmCcGeoele implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_geoele_id
     */
    @Column(name="c_geoele_id",length = 36)
    private String cGeoeleId;

    /**
     * c_geodesc
     */
    @Column(name="c_geodesc",length = 200)
    private String cGeodesc;

    /**
     * c_geotype
     */
    @Column(name="c_geotype",length = 3)
    private String cGeotype;

    /**
     * c_geomark
     */
    @Column(name="c_geomark",length = 100)
    private String cGeomark;

    /**
     * c_westbl
     */
    @Column(name="c_westbl",length = 10)
    private String cWestbl;

    /**
     * c_eastbl
     */
    @Column(name="c_eastbl",length = 10)
    private String cEastbl;

    /**
     * c_southbl
     */
    @Column(name="c_southbl",length = 10)
    private String cSouthbl;

    /**
     * c_northbl
     */
    @Column(name="c_northbl",length = 10)
    private String cNorthbl;

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

    public TabOminCmCcGeoele() {
    }

}


