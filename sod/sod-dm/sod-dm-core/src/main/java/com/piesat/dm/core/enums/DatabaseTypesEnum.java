package com.piesat.dm.core.enums;

import com.piesat.dm.core.parser.DatabaseInfo;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author cuiwenhui
 */

public enum DatabaseTypesEnum {
    XUGU("XUGU"),GBASE("GBASE"),CASSANDRA("CASSANDRA"),POSTGRESQL("POSTGRESQL");

    /**
     * 数据库名称
     */
    private String name;

    DatabaseTypesEnum(String name) {
        this.name = name;
    }

    public static DatabaseTypesEnum match(String name) {
        if (name != null) {
            for (DatabaseTypesEnum item : DatabaseTypesEnum.values()) {
                if (item.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        }
        return null;
    }


    public String getName() {
        return name;
    }
}
