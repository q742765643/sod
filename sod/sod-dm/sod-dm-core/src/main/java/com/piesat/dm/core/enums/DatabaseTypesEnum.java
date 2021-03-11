package com.piesat.dm.core.enums;

import com.piesat.dm.core.action.exc.abs.ExcAbs;
import com.piesat.dm.core.action.exc.impl.ExcImpl;
import com.piesat.dm.core.action.exc.impl.cus.CassandraExcImpl;
import com.piesat.dm.core.action.impl.CassandraDatabaseImpl;
import com.piesat.dm.core.action.impl.DatabaseImpl;
import com.piesat.dm.core.action.impl.abs.BaseAbs;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.dm.core.template.*;
import com.piesat.util.ResultT;

/**
 * @author cuiwenhui
 */

public enum DatabaseTypesEnum {
    XUGU("XUGU", new DatabaseImpl(), new ExcImpl(), new SqlTemplateXuGu())
    , GBASE("GBASE8A", new DatabaseImpl(), new ExcImpl(), new SqlTemplateGbase())
    , CASSANDRA("CASSANDRA", new CassandraDatabaseImpl(), new CassandraExcImpl(), new SqlTemplateCassandra())
    , POSTGRESQL("POSTGRESQL", new DatabaseImpl(), new ExcImpl(), new SqlTemplatePostgreSql());

    /**
     * 数据库名称
     */
    private String name;

    private BaseAbs baseAbs;

    private ExcAbs excAbs;

    private SqlTemplateComm st;

    DatabaseTypesEnum(String name, BaseAbs baseAbs, ExcAbs excAbs, SqlTemplateComm st) {
        this.name = name;
        this.baseAbs = baseAbs;
        this.excAbs = excAbs;
        this.st = st;
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

    public ExcAbs build(ConnectVo c, ResultT r){
        BaseAbs ba = this.getBaseAbs().init(c, r);
        return this.getExcAbs().init(ba,this.getSt());
    }

    public BaseAbs getConn(ConnectVo c, ResultT r){
        return this.getBaseAbs().init(c, r);
    }

    public String getName() {
        return name;
    }

    public BaseAbs getBaseAbs() {
        return baseAbs;
    }

    public ExcAbs getExcAbs() {
        return excAbs;
    }

    public SqlTemplateComm getSt() {
        return st;
    }
}
