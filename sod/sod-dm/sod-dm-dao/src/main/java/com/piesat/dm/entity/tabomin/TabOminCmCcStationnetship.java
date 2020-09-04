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
 *  tab_omin_cm_cc_stationnetship
 * @author 大狼狗 2020-05-18
 */
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_STATIONNETSHIP")
public class TabOminCmCcStationnetship implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_snetship_id
     */
    @Column(name="c_snetship_id",length = 50)
    private String cSnetshipId;

    /**
     * c_siteopf_id
     */
    @Column(name="c_siteopf_id",length = 36)
    private String cSiteopfId;

    /**
     * c_snet_id
     */
    @Column(name="c_snet_id",length = 36)
    private String cSnetId;

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
     * c_station_level
     */
    @Column(name="c_station_level",length = 36)
    private String cStationLevel;

    /**
     * c_onduty
     */
    @Column(name="c_onduty",length = 36)
    private String cOnduty;

    /**
     * c_modifier
     */
    @Column(name="c_modifier",length = 36)
    private String cModifier;

    /**
     * c_opt_type
     */
    @Column(name="c_opt_type",length = 8)
    private String cOptType;

    /**
     * is_check
     */
    @Column(name="is_check",length = 10)
    private String isCheck;

    /**
     * c_instr_model
     */
    @Column(name="c_instr_model",length = 64)
    private String cInstrModel;

    /**
     * c_mf
     */
    @Column(name="c_mf",length = 64)
    private String cMf;

    /**
     * c_power_type
     */
    @Column(name="c_power_type",length = 36)
    private String cPowerType;

    /**
     * c_installtime
     */
    @Column(name="c_installtime",length = 36)
    private String cInstalltime;

    /**
     * c_enabletime
     */
    @Column(name="c_enabletime",length = 32)
    private String cEnabletime;

    /**
     * c_install_pos
     */
    @Column(name="c_install_pos",length = 256)
    private String cInstallPos;

    /**
     * c_radar_phnum
     */
    @Column(name="c_radar_phnum",length = 256)
    private String cRadarPhnum;

    /**
     * version
     */
    @Column(name="version")
    private Integer version;

    /**
     * c_obserway
     */
    @Column(name="c_obserway",length = 10)
    private String cObserway;

    /**
     * c_indexnbr
     */
    @Column(name="c_indexnbr",length = 32)
    private String cIndexnbr;

    /**
     * c_exchangec
     */
    @Column(name="c_exchangec",length = 3)
    private String cExchangec;

    /**
     * c_startdate
     */
    @Column(name="c_startdate",length = 8)
    private String cStartdate;

    /**
     * c_enddate
     */
    @Column(name="c_enddate",length = 8)
    private String cEnddate;

    /**
     * c_starttime
     */
    @Column(name="c_starttime",length = 20)
    private String cStarttime;

    /**
     * c_endtime
     */
    @Column(name="c_endtime",length = 20)
    private String cEndtime;

    /**
     * c_timesystem
     */
    @Column(name="c_timesystem",length = 20)
    private String cTimesystem;

    /**
     * c_exchangecode
     */
    @Column(name="c_exchangecode",length = 30)
    private String cExchangecode;

    /**
     * c_obsvmode
     */
    @Column(name="c_obsvmode",length = 3)
    private String cObsvmode;

    /**
     * c_obsvcount
     */
    @Column(name="c_obsvcount",length = 20)
    private String cObsvcount;

    /**
     * c_obsvtimes
     */
    @Column(name="c_obsvtimes",length = 100)
    private String cObsvtimes;

    public TabOminCmCcStationnetship() {
    }

}
