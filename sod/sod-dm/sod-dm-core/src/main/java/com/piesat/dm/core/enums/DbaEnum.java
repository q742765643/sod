package com.piesat.dm.core.enums;

import com.piesat.dm.core.constants.AlterTableConstants;

/**
 * @author cuiwenhui
 */

public enum DbaEnum {
    READ(AlterTableConstants.ALTER_TABLE_1), WRITE(AlterTableConstants.ALTER_TABLE_2),
    CREATE(AlterTableConstants.ALTER_TABLE_3), DROP(AlterTableConstants.ALTER_TABLE_4),
    ALL(AlterTableConstants.ALTER_TABLE_0);

    private String dba;

    public String getDba() {
        return dba;
    }

    DbaEnum(String dba) {
        this.dba = dba;
    }

}
