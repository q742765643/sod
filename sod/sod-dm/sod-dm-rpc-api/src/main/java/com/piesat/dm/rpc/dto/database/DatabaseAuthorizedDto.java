package com.piesat.dm.rpc.dto.database;

import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2020年 12月21日 12:00:23
 */
@Data
public class DatabaseAuthorizedDto extends BaseDto {

    private String databaseUsername;

    private String databaseId;

    private Integer status;

    private String opeType;

    private String msg;
}
