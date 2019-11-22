package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月21日 16:32:39
 */
@Data
public class LogicDefineDto {
    private static final long serialVersionUID = 1L;

    /**
     * logic_id
     */
    private String logicId;

    /**
     * logic_name
     */
    private String logicName;

    /**
     * serial_number
     */
    private Integer serialNumber;

    /**
     * logic_desc
     */
    private String logicDesc;

}
