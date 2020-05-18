package com.piesat.dm.entity.tabomin;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-15 19:08
 **/
@Entity
@Data
@Table(name="TAB_OMIN_CM_CC_DDDDOCAUTHINFO")
public class TabOminCmCcDdddocauthinfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    /**
     * c_docauth_id
     */
    @Column(name = "c_docauth_id",length =36 )
    private String cDocauthId;

    /**
     * c_docindnname
     */
    @Column(name = "c_docindnname",length =300)
    private String cDocindnname;

    /**
     * c_docorgname
     */
    @Column(name = "c_docorgname",length =600)
    private String cDocorgname;

    /**
     * c_docphone
     */
    @Column(name = "c_docphone",length =50)
    private String cDocphone;

    /**
     * c_coremeta_id
     */
    @Column(name = "c_coremeta_id",length =36)
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
    @Column(name = "c_opt_type",length =128)
    private String cOptType;

    public TabOminCmCcDdddocauthinfo() {
    }

}

