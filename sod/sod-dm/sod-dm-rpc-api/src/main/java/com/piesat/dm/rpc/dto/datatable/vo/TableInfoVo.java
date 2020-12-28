package com.piesat.dm.rpc.dto.datatable.vo;

import lombok.Data;
import org.springframework.cglib.beans.BeanMap;

import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2020年 12月24日 10:50:35
 */
@Data
public class TableInfoVo {
    private String tableNameCn;
    private String tableName;
    private String databaseId;
    private String databasePid;
    private String className;
    private String classId;
    private String dataId;
    public Map<String,String> getMap(){
       return BeanMap.create(this);
    }
}
