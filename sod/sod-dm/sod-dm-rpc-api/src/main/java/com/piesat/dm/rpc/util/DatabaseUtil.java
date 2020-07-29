package com.piesat.dm.rpc.util;

import com.piesat.dm.core.api.DatabaseDcl;
import com.piesat.dm.core.api.impl.Cassandra;
import com.piesat.dm.core.api.impl.Gbase8a;
import com.piesat.dm.core.api.impl.PostgreSql;
import com.piesat.dm.core.api.impl.Xugu;
import com.piesat.dm.core.parser.DatabaseInfo;
import com.piesat.dm.dao.database.DatabaseDefineDao;
import com.piesat.dm.rpc.dto.database.DatabaseAdministratorDto;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
import com.piesat.dm.rpc.dto.database.DatabaseDto;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
        String databaseType = database.getDatabaseDefine().getDatabaseType().toLowerCase();
        String databaseUrl = database.getDatabaseDefine().getDatabaseUrl();
        String databaseIp = database.getDatabaseDefine().getDatabaseIp();
        int port = Integer.parseInt(database.getDatabaseDefine().getDatabasePort());
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
        } else if (databaseInfo.getPostgresql().equals(databaseType)) {
            db = new PostgreSql(databaseUrl, dad.getUserName(), dad.getPassWord());
        }
        return db;
    }

    public static DatabaseDcl getPubDatabase(DatabaseDto database, DatabaseInfo databaseInfo) throws Exception {
        DatabaseDcl db = null;
        String databaseType = database.getDatabaseDefine().getDatabaseType().toLowerCase();
        String databaseUrl = database.getDatabaseDefine().getDatabaseUrl();
        String databaseIp = database.getDatabaseDefine().getDatabaseIp();
        int port = Integer.parseInt(database.getDatabaseDefine().getDatabasePort());
        Set<DatabaseAdministratorDto> dal = database.getDatabaseDefine().getDatabaseAdministratorList();
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

    public static DatabaseDcl getDatabaseDefine(DatabaseDefineDto databaseDefineDto, DatabaseInfo databaseInfo) throws Exception {
        DatabaseDcl db = null;
        String databaseType = databaseDefineDto.getDatabaseType().toLowerCase();
        String databaseUrl = databaseDefineDto.getDatabaseUrl();
        String databaseIp = databaseDefineDto.getDatabaseIp();
        int port = Integer.parseInt(databaseDefineDto.getDatabasePort());
        Set<DatabaseAdministratorDto> dal = databaseDefineDto.getDatabaseAdministratorList();
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
