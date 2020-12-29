package com.piesat.dm.rpc.util;

import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Cassandra;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.PostgreSql;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.dm.rpc.dto.database.SchemaDto;

import java.util.List;
import java.util.Set;

/**
 * 数据库
 *
 * @author cwh
 * @date 2020年 02月13日 16:45:23
 */

public class DatabaseUtil {


    public static DatabaseDcl getDatabase(SchemaDto database, DatabaseInfo databaseInfo) throws Exception {
        DatabaseDcl db = null;
        String databaseType = database.getDatabaseDto().getDatabaseType().toLowerCase();
        String databaseUrl = database.getDatabaseDto().getDatabaseUrl();
        String databaseIp = database.getDatabaseDto().getDatabaseIp();
        int port = Integer.parseInt(database.getDatabaseDto().getDatabasePort());
        Set<DatabaseAdministratorDto> dal = database.getDatabaseDto().getDatabaseAdministratorList();
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
        } else if (databaseInfo.getPostgresql().equals(databaseType)) {
            db = new PostgreSql(databaseUrl, dad.getUserName(), dad.getPassWord());
        }
        return db;
    }

    public static DatabaseDcl getPubDatabase(SchemaDto database, DatabaseInfo databaseInfo) throws Exception {
        DatabaseDcl db = null;
        String databaseType = database.getDatabaseDto().getDatabaseType().toLowerCase();
        String databaseUrl = database.getDatabaseDto().getDatabaseUrl();
        String databaseIp = database.getDatabaseDto().getDatabaseIp();
        int port = Integer.parseInt(database.getDatabaseDto().getDatabasePort());
        Set<DatabaseAdministratorDto> dal = database.getDatabaseDto().getDatabaseAdministratorList();
        DatabaseAdministratorDto dad = null;
        if (dal.size()>0){
            dad = dal.iterator().next();
        }
        if (dad == null) {
            throw new Exception("数据库用户不存在");
        }
        if (databaseInfo.getXugu().equals(databaseType)) {
            db = new Xugu(databaseUrl, dad.getUserName(), dad.getPassWord());
        } else if (databaseInfo.getGbase8a().equals(databaseType)) {
            db = new Gbase8a(databaseUrl, dad.getUserName(), dad.getPassWord());
        } else if (databaseInfo.getCassandra().equals(databaseType)) {
            db = new Cassandra(databaseIp, port, dad.getUserName(), dad.getPassWord(), database.getSchemaName());
        } else if (databaseInfo.getPostgresql().equals(databaseType.toLowerCase())) {
            db = new PostgreSql(databaseUrl, dad.getUserName(), dad.getPassWord());
        }
        return db;
    }

    public static DatabaseDcl getDatabaseDefine(DatabaseDto databaseDto, DatabaseInfo databaseInfo) throws Exception {
        DatabaseDcl db = null;
        String databaseType = databaseDto.getDatabaseType().toLowerCase();
        String databaseUrl = databaseDto.getDatabaseUrl();
        String databaseIp = databaseDto.getDatabaseIp();
        int port = Integer.parseInt(databaseDto.getDatabasePort());
        Set<DatabaseAdministratorDto> dal = databaseDto.getDatabaseAdministratorList();
        DatabaseAdministratorDto dad = null;
        boolean b = dal.stream().anyMatch(DatabaseAdministratorDto::getIsManager);
        if (!b) {
            throw new Exception("数据库管理用户不存在");
        }
        dad = dal.stream().filter(DatabaseAdministratorDto::getIsManager).findFirst().get();

        if (databaseInfo.getXugu().toLowerCase().equals(databaseType)) {
            db = new Xugu(databaseUrl, dad.getUserName(), dad.getPassWord());
        } else if (databaseInfo.getGbase8a().toLowerCase().equals(databaseType)) {
            db = new Gbase8a(databaseUrl, dad.getUserName(), dad.getPassWord());
        } else if (databaseInfo.getCassandra().toLowerCase().equals(databaseType)) {
            db = new Cassandra(databaseIp, port, dad.getUserName(), dad.getPassWord(), null);
        }
        return db;
    }


    public static DatabaseAdministratorDto getManagerUser(List<DatabaseAdministratorDto> list) {
        DatabaseAdministratorDto m = null;
        for (DatabaseAdministratorDto d : list) {
            if (d.getIsManager()) {
                m = d;
            }
        }
        return m;
    }

}
