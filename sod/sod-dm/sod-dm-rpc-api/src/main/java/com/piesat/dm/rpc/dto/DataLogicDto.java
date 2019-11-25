package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * 资料用途分类
 *
 * @author cwh
 * @date 2019年 11月20日 17:04:05
 */
@Data
public class DataLogicDto {
    private static final long serialVersionUID = 1L;

    /**
     * class_logic_id
     */
    private String classLogicId;

    /**
     * data_class_id
     */
    private String dataClassId;

    /**
     * logic_id
     */
    private String logicId;

    /**
     * storage_type
     */
    private String storageType;

    /**
     * database_type
     */
    private String databaseType;
}
