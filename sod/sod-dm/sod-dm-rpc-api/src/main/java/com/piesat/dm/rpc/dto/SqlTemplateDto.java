package com.piesat.dm.rpc.dto;

import lombok.Data;

/**
 * sql模板
 *
 * @author cwh
 * @date 2020年 02月05日 20:55:07
 */
@Data
public class SqlTemplateDto {
    private String database_server;
    private String database_name;
    private String template;
}
