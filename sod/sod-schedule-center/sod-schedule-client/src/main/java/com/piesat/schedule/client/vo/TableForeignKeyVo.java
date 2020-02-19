package com.piesat.schedule.client.vo;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-19 15:26
 **/
@Data
public class TableForeignKeyVo {
    private String classLogicId;

    private String keyColumn;

    private String eleColumn;

    private Boolean isReal;
}

