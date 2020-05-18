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
 *  tab_omin_cm_cc_tempele
 * @author 大狼狗 2020-05-18
 */
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_TEMPELE")
public class TabOminCmCcTempele implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_tempele_id
     */
    @Column(name="c_tempele_id",length = 36)
    private String cTempeleId;

    /**
     * c_begin
     */
    @Column(name="c_begin",length = 8)
    private String cBegin;

    /**
     * c_end
     */
    @Column(name="c_end",length = 8)
    private String cEnd;

    /**
     * c_obsfreq
     */
    @Column(name="c_obsfreq",length = 128)
    private String cObsfreq;

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
    @Column(name="c_opt_type",length = 8)
    private String cOptType;

    public TabOminCmCcTempele() {
    }

}
