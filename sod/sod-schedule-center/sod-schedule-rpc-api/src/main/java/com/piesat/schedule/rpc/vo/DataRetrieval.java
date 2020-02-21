package com.piesat.schedule.rpc.vo;

import lombok.Data;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-21 09:22
 **/
@Data
public class DataRetrieval {
    private String tableName;
    private String vTableName;
    private String ddataId;
    private String parentId;
    private String databaseType;
    private String profileName;
    private String foreignKey;
    private String primaryKey;

}

