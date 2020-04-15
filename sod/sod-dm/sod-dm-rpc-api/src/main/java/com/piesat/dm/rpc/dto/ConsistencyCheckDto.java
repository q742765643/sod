package com.piesat.dm.rpc.dto;

import com.piesat.common.jpa.BaseDao;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.util.BaseDto;
import lombok.Data;


/**
 * @author yaya
 * @description TODO
 * @date 2020/2/14 11:03
 */
@Data
public class ConsistencyCheckDto extends BaseDto {

    /**
     * 与数据库关联id
     * database_id
     */
    private String databaseId;

    /**
     * 数据库名称
     */
    private String databaseName;

    private DatabaseDto databaseDto;
}
