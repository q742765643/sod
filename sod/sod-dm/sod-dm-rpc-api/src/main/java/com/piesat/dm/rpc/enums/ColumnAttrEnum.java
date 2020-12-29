package com.piesat.dm.rpc.enums;

import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.rpc.vo.PossessAttribute;

/**
 * @author cuiwenhui
 */

public enum ColumnAttrEnum {
    XUGU("XUGU",new PossessAttribute(true,true,true))
    ,GBASE("GBASE8A",new PossessAttribute(true,true,true))
    ,CASSANDRA("CASSANDRA",new PossessAttribute(false,false,false))
    ,POSTGRESQL("POSTGRESQL",new PossessAttribute(true,true,true));

    /**
     * 数据库名称
     */
    private String name;
    private PossessAttribute possessAttribute;

    ColumnAttrEnum(String name,PossessAttribute possessAttribute){
        this.name = name;
        this.possessAttribute = possessAttribute;
    }

    public String getName() {
        return name;
    }

    public PossessAttribute getPossessAttribute() {
        return possessAttribute;
    }

    public static ColumnAttrEnum match(String name) {
        if (name != null) {
            for (ColumnAttrEnum item : ColumnAttrEnum.values()) {
                if (item.getName().equalsIgnoreCase(name)) {
                    return item;
                }
            }
        }
        return null;
    }
}
