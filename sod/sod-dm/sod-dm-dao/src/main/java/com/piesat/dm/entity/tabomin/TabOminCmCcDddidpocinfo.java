package com.piesat.dm.entity.tabomin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-15 19:10
 **/
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_DDDIDPOCINFO")
public class TabOminCmCcDddidpocinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_idpoc_id
     */
    @Column(name = "c_idpoc_id",length = 36)
    private String cIdpocId;

    /**
     * c_rpindname
     */
    @Column(name = "c_rpindname",length = 300)
    private String cRpindname;

    /**
     * c_rporgname
     */
    @Column(name = "c_rporgname",length = 600)
    private String cRporgname;

    /**
     * c_cntphone
     */
    @Column(name = "c_cntphone",length = 50)
    private String cCntphone;

    /**
     * c_coremeta_id
     */
    @Column(name = "c_coremeta_id",length = 36)
    private String cCoremetaId;

    /**
     * c_create_date
     */
    @Column(name = "c_create_date")
    private Date cCreateDate;

    /**
     * c_updated_date
     */
    @Column(name = "c_updated_date")
    private Date cUpdatedDate;

    /**
     * version
     */
    @Column(name = "version")
    private Integer version;

    /**
     * c_opt_type
     */
    @Column(name = "c_opt_type",length = 128)
    private String cOptType;

    public TabOminCmCcDddidpocinfo() {
    }

}
