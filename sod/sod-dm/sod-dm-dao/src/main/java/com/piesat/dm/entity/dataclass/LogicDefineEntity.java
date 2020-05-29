package com.piesat.dm.entity.dataclass;

import com.piesat.common.annotation.Excel;
import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月21日 16:32:39
 */
@Data
@Table(name = "T_SOD_LOGIC_DEFINE")
@Entity
//@Proxy(lazy = false)
public class LogicDefineEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;
    @Excel(name = "序号")
    @Transient
    private int num;
    /**
     * logic_id
     */
    @Excel(name = "数据库用途ID")
    @Column(name = "logic_flag", length = 255, nullable = false)
    private String logicFlag;

    /**
     * logic_name
     */
    @Excel(name = "用途描述")
    @Column(name = "logic_name", length = 255, nullable = false)
    private String logicName;

    /**
     * serial_number
     */
    //@Excel(name = "排序")
    @Column(name = "serial_number")
    private Integer serialNumber;

    @Excel(name = "表类型")
    @Transient
    private String tableType;

    @Excel(name = "数据库类型")
    @Transient
    private String databaseType;

    /**
     * logic_desc
     */
    @Excel(name = "用途说明")
    @Column(name = "logic_desc", length = 255)
    private String logicDesc;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="logic_id")
    private Set<LogicStorageTypesEntity> logicStorageTypesEntityList = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name="logic_id")
    private Set<LogicDatabaseEntity> logicDatabaseEntityList = new HashSet<>();
}
