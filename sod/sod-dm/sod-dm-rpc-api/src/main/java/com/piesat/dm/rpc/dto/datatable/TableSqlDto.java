package com.piesat.dm.rpc.dto.datatable;

import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;

/**
 * 表sql
 *
 * @author cwh
 * @date 2020年 04月02日 14:02:47
 */
@Data
public class TableSqlDto extends BaseDto {

    private String databaseId;

    private Boolean delOld;

    private String tableName ;

    private String tableSql ;
}
