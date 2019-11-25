package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月20日 17:04:53
 */
@Data
public class DataTableDto {
    private static final long serialVersionUID = 1L;

    /**
     * table_id
     */
    private String tableId;

    /**
     * class_logic_id
     */
    private String classLogicId;

    /**
     * data_service_id
     */
    private String dataServiceId;


    /**
     * table_name
     */
    private String tableName;


    /**
     * db_table_type
     */
    private String dbTableType;

    /**
     * table_desc
     */
    private String tableDesc;

    /**
     * data_service_name
     */
    private String dataServiceName;

    /**
     * name_cn
     */
    private String nameCn;

    /**
     * creator
     */
    private String creator;

    /**
     * user_id
     */
    private String userId;

}
