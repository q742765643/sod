package com.piesat.common.jpa.dialect;

import org.hibernate.dialect.MySQL57Dialect;
import org.hibernate.dialect.MySQL5Dialect;
import org.hibernate.dialect.MySQLDialect;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 14:24
 */
public class XuguDialect extends MySQLDialect {
    @Override
    public String getTableTypeString() {
        return "";
    }
}
