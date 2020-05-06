package com.piesat.dm.entity.dataclass;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 资料基础信息
 *
 * @author cwh
 * @date 2020年 04月01日 15:25:49
 */
@Data
@Table(name = "T_SOD_DATACLASS_BASEINFO")
@Entity
public class DataClassBaseInfoEntity extends BaseEntity {

    @Column(name = "data_class_id",columnDefinition = "varchar(225) default ''")
    private String dataClassId;

    @Column(name = "c_coremeta_id",columnDefinition = "varchar(225) default ''")
    private String cCoremetaId;

    @Column(name = "c_idabs",columnDefinition = "varchar(225) default ''")
    private String cIdabs;

    @Column(name = "c_mainfreq",columnDefinition = "varchar(225) default ''")
    private String cMainfreq;

    @Column(name = "c_datascal",columnDefinition = "varchar(225) default ''")
    private String cDatascal;

    @Column(name = "c_westbl",columnDefinition = "varchar(225) default ''")
    private String cWestbl;

    @Column(name = "c_eastbl",columnDefinition = "varchar(225) default ''")
    private String cEastbl;

    @Column(name = "c_southbl",columnDefinition = "varchar(225) default ''")
    private String cSouthbl;

    @Column(name = "c_northbl",columnDefinition = "varchar(225) default ''")
    private String cNorthbl;

    @Column(name = "c_geodesc",columnDefinition = "varchar(225) default ''")
    private String cGeodesc;

    @Column(name = "c_source",columnDefinition = "varchar(225) default ''")
    private String cSource;

    @Column(name = "c_netname",columnDefinition = "varchar(225) default ''")
    private String cNetname;

    @Column(name = "c_nettype",columnDefinition = "varchar(225) default ''")
    private String cNettype;

    @Column(name = "c_obsfreq",columnDefinition = "varchar(225) default ''")
    private String cObsfreq;

    @Column(name = "c_begin",columnDefinition = "varchar(225) default ''")
    private String cBegin;

    @Column(name = "c_end",columnDefinition = "varchar(225) default ''")
    private String cEnd;

    @Column(name = "c_creat_name",columnDefinition = "varchar(225) default ''")
    private String cCreatName;

    @Column(name = "c_checker",columnDefinition = "varchar(225) default ''")
    private String cChecker;

    @Column(name = "c_rpindname",columnDefinition = "varchar(225) default ''")
    private String cRpindname;

    @Column(name = "c_rporgname",columnDefinition = "varchar(225) default ''")
    private String cRporgname;

    @Column(name = "c_docindnname",columnDefinition = "varchar(225) default ''")
    private String cDocindnname;

    @Column(name = "use_base_info",columnDefinition = "varchar(225) default ''")
    private String useBaseInfo;

}
