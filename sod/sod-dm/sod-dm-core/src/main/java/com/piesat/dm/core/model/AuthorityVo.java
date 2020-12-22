package com.piesat.dm.core.model;

import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.enums.DbaEnum;
import lombok.Data;

/**
 * @author cwh
 * @date 2020年 12月04日 14:20:01
 */
@Data
public class AuthorityVo {
    private String schema;
    private String tableName;
    private String userName;
    private DbaEnum dbaEnum;

    public AuthorityVo(){}
    public AuthorityVo(String schema,String tableName,String userName,DbaEnum dbaEnum){
        this.schema = schema;
        this.tableName = tableName;
        this.userName = userName;
        this.dbaEnum = dbaEnum;
    }

    public void setSchema(String schema) {
        this.schema = schema.toUpperCase();
    }

    public void setTableName(String tableName) {
        this.tableName = tableName.toUpperCase();
    }

    public void setDbaEnum(DbaEnum dbaEnum) {
        this.dbaEnum = dbaEnum;
        this.grantStr = dbaEnum.getDba();
    }

    public void format(DatabaseTypesEnum databaseType){
        if (databaseType.equals(DatabaseTypesEnum.CASSANDRA)){
            this.schema = schema.toLowerCase();
            this.tableName = tableName.toLowerCase();
        }else {
            this.schema = schema.toUpperCase();
            this.tableName = tableName.toUpperCase();
        }
    }

    private String grantStr;

}
