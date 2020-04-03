package com.piesat.dm.rpc.dto;

import com.piesat.dm.rpc.dto.database.DatabaseDto;
import lombok.Data;


/**
 * @author yaya
 * @description TODO
 * @date 2020/2/14 11:03
 */
@Data
public class ConsistencyCheckDto {

    /**
     * 与数据库定义关联id
     * database_id
     */
    private DatabaseDto databaseDto;

    /**
     * 数据库名称
     */
    private String databaseName;
}
