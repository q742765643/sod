package com.piesat.dm.rpc.vo;

import lombok.Data;
import org.springframework.cglib.beans.BeanMap;

import java.util.ArrayList;
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
    private String userId;
    private String onlyMy;
    private String all;

    public Map<String, Object> getMap() {
        boolean b = !"1".equals(all) && (classIds == null || classIds.size() == 0);
        if (b) {
            classIds = new ArrayList<>();
            classIds.add("PLACEHOLDER");
        }
        return BeanMap.create(this);
    }
}
