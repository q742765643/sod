package com.piesat.dm.entity;

import com.piesat.common.jpa.entity.BaseEntity;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月21日 16:32:39
 */
@Data
@Table(name = "T_SOD_LOGIC_DEFINE")
@Entity
public class LogicDefineEntity extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * logic_id
     */
    @Column(name = "logic_id", length = 255, nullable = false)
    private String logicId;

    /**
     * logic_name
     */
    @Column(name = "logic_name", length = 255, nullable = false)
    private String logicName;

    /**
     * serial_number
     */
    @Column(name = "serial_number")
    private Integer serialNumber;

    /**
     * logic_desc
     */
    @Column(name = "logic_desc", length = 255)
    private String logicDesc;

}
