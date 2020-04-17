package com.piesat.dm.rpc;

import lombok.Data;

/**
 * 存储概览参数
 *
 * @author cwh
 * @date 2020年 04月10日 16:09:25
 */
@Data
public class StorageConfigParam {
    private String databaseName;
    private String specialDatabaseName;
    private String className;
    private String parentId;
    private String dDataId;
    private String logicName;
    private String tableName;
    private String dataClassId;
    private String cElementCode;
    private String dbEleCode;
    private String userEleCode;
    private String eleName;
    private String type;
    private String accuracy;
}
