package com.piesat.dm.rpc.dto.database;

import com.piesat.util.BaseDto;
import lombok.Data;

import javax.persistence.Column;

/**
 * 数据库用户审核日志
 * @author cwh
 * @date 2020年 12月16日 15:07:59
 */
@Data
public class DbUserAlterLogDto extends BaseDto {
    private String log;

    private String opeType;

    private Boolean status;
}
