package com.piesat.dm.entity.tabomin;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-18 15:15
 **/
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
 *  tab_omin_cm_cc_dddinfo
 * @author 大狼狗 2020-05-18
 */
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_DDDINFO")
public class TabOminCmCcDddinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_coremeta_id
     */
    @Column(name="c_coremeta_id",length = 36)
    private String cCoremetaId;

    /**
     * c_dsver
     */
    @Column(name="c_dsver",length = 20)
    private String cDsver;

    /**
     * c_dsdatest
     */
    @Column(name="c_dsdatest",length = 8)
    private String cDsdatest;

    /**
     * c_filename
     */
    @Column(name="c_filename",columnDefinition = "TEXT")
    private String cFilename;

    /**
     * c_filecont
     */
    @Column(name="c_filecont",columnDefinition = "TEXT")
    private String cFilecont;

    /**
     * c_eigvalue
     */
    @Column(name="c_eigvalue",columnDefinition = "TEXT")
    private String cEigvalue;

    /**
     * c_pretime
     */
    @Column(name="c_pretime",length = 200)
    private String cPretime;

    /**
     * c_format
     */
    @Column(name="c_format",length = 30)
    private String cFormat;

    /**
     * c_read
     */
    @Column(name="c_read",columnDefinition = "TEXT")
    private String cRead;

    /**
     * c_medname
     */
    @Column(name="c_medname",length = 30)
    private String cMedname;

    /**
     * c_medvol
     */
    @Column(name="c_medvol",length = 200)
    private String cMedvol;

    /**
     * c_meddirec
     */
    @Column(name="c_meddirec",columnDefinition = "TEXT")
    private String cMeddirec;

    /**
     * c_meddata
     */
    @Column(name="c_meddata",length = 1500)
    private String cMeddata;

    /**
     * c_archmode
     */
    @Column(name="c_archmode",length = 3)
    private String cArchmode;

    /**
     * c_archtime
     */
    @Column(name="c_archtime",length = 200)
    private String cArchtime;

    /**
     * c_tempextent
     */
    @Column(name="c_tempextent",length = 360)
    private String cTempextent;

    /**
     * c_timeresol
     */
    @Column(name="c_timeresol",length = 3)
    private String cTimeresol;

    /**
     * c_obsertime
     */
    @Column(name="c_obsertime",length = 250)
    private String cObsertime;

    /**
     * c_distype
     */
    @Column(name="c_distype",length = 1500)
    private String cDistype;

    /**
     * c_stainfo
     */
    @Column(name="c_stainfo",length = 200)
    private String cStainfo;

    /**
     * c_sites
     */
    @Column(name="c_sites",length = 200)
    private String cSites;

    /**
     * c_grid
     */
    @Column(name="c_grid",length = 200)
    private String cGrid;

    /**
     * c_sampdis
     */
    @Column(name="c_sampdis",length = 200)
    private String cSampdis;

    /**
     * c_vertproj
     */
    @Column(name="c_vertproj",length = 500)
    private String cVertproj;

    /**
     * c_obserinstru
     */
    @Column(name="c_obserinstru",columnDefinition = "TEXT")
    private String cObserinstru;

    /**
     * c_dprocess
     */
    @Column(name="c_dprocess",columnDefinition = "TEXT")
    private String cDprocess;

    /**
     * c_qualcontr
     */
    @Column(name="c_qualcontr",columnDefinition = "TEXT")
    private String cQualcontr;

    /**
     * c_qualassess
     */
    @Column(name="c_qualassess",columnDefinition = "TEXT")
    private String cQualassess;

    /**
     * c_dcomplete
     */
    @Column(name="c_dcomplete",columnDefinition = "TEXT")
    private String cDcomplete;

    /**
     * c_dscitation
     */
    @Column(name="c_dscitation",columnDefinition = "TEXT")
    private String cDscitation;

    /**
     * c_dsother
     */
    @Column(name="c_dsother",columnDefinition = "TEXT")
    private String cDsother;

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

    public TabOminCmCcDddinfo() {
    }

}
