package com.piesat.dm.rpc.util;

import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Cassandra;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.rpc.dto.DatabaseAdministratorDto;
import com.piesat.dm.rpc.dto.DatabaseDto;

import java.util.Set;

/**
 * 数据库
 *
 * @author cwh
 * @date 2020年 02月13日 16:45:23
 */

public class DatabaseUtil {


    public static DatabaseDcl getDatabase(DatabaseDto database, DatabaseInfo databaseInfo) throws Exception {
        DatabaseDcl db = null;
        String databaseType = database.getDatabaseDefine().getDatabaseType();
        String databaseUrl = database.getDatabaseDefine().getDatabaseUrl();
        String databaseIp = database.getDatabaseDefine().getDatabaseIp();
        int port = Integer.getInteger(database.getDatabaseDefine().getDatabasePort());
        Set<DatabaseAdministratorDto> dal = database.getDatabaseDefine().getDatabaseAdministratorList();
        DatabaseAdministratorDto dad = null;
        for (DatabaseAdministratorDto da : dal) {
            if (da.getIsManager()) {
                dad = da;
            }
        }
        if (dad == null) {
            throw new Exception("数据库管理用户不存在");
        }
        if (databaseInfo.getXugu().equals(databaseType)) {
            db = new Xugu(databaseUrl, dad.getUserName(), dad.getPassWord());
        } else if (databaseInfo.getGbase8a().equals(databaseType)) {
            db = new Gbase8a(databaseUrl, dad.getUserName(), dad.getPassWord());
        } else if (databaseInfo.getCassandra().equals(databaseType)) {
            db = new Cassandra(databaseIp, port, dad.getUserName(), dad.getPassWord(), database.getSchemaName());
        }
        return db;
    }

}