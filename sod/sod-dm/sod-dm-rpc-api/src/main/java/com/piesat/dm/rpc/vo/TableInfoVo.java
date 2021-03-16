package com.piesat.dm.rpc.vo;

import lombok.Data;
import org.springframework.cglib.beans.BeanMap;

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2020年 12月24日 10:50:35
 */
@Data
public class TableInfoVo {
    private List<String> classIds;
    private String tableNameCn;
    private String tableName;
    private String databaseId;
    private String databasePid;
    private String schemaNameCn;
    private String databaseName;
    private String className;
    private String classId;
    private String dataId;
    public Map<String,Object> getMap(){
       return BeanMap.create(this);
    }
}
