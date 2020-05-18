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
 *  tab_omin_cm_cc_stationnet
 * @author 大狼狗 2020-05-18
 */
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_STATIONNET")
public class TabOminCmCcStationnet implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_snet_id
     */
    @Column(name="c_snet_id",length = 36)
    private String cSnetId;

    /**
     * c_netname
     */
    @Column(name="c_netname",length = 50)
    private String cNetname;

    /**
     * c_netvalue
     */
    @Column(name="c_netvalue",length = 50)
    private String cNetvalue;

    /**
     * c_netcode
     */
    @Column(name="c_netcode",length = 50)
    private String cNetcode;

    /**
     * c_desc
     */
    @Column(name="c_desc",columnDefinition = "TEXT")
    private String cDesc;

    /**
     * c_status
     */
    @Column(name="c_status",length = 32)
    private String cStatus;

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
     * c_flow_id
     */
    @Column(name="c_flow_id",length = 32)
    private String cFlowId;

    /**
     * c_opt_type
     */
    @Column(name="c_opt_type",length = 8)
    private String cOptType;

    /**
     * c_ags_type
     */
    @Column(name="c_ags_type",length = 36)
    private String cAgsType;

    /**
     * version
     */
    @Column(name="version")
    private Integer version;

    public TabOminCmCcStationnet() {
    }

}

