package com.piesat.common.jpa.dialect;

import org.hibernate.dialect.MySQL5Dialect;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/11/18 14:24
 */
public class XuguDialect extends MySQL5Dialect {
    @Override
    public String getTableTypeString() {
        return "";
    }
}
