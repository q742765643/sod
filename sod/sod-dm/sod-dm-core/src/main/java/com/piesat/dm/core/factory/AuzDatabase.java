package com.piesat.dm.core.factory;

import cn.hutool.core.util.ArrayUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.common.util.TemplateUtil;
import com.piesat.dm.core.constants.Constants;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.*;
import com.piesat.dm.core.parser.ManagerUser;
import com.piesat.dm.core.template.SqlTemplate;
import com.piesat.util.ResultT;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @date 2020年 12月08日 19:03:37
 */
public class AuzDatabase extends AuzBase {
    public AuzDatabase(DatabaseTypesEnum databaseType, Object con) {
        super(databaseType, con);
    }

    /*-----------------------------------用户-----------------------------------*/

    @Override
    public void doCreateUser(UserInfo userInfo, ResultT<String> resultT) {
        if (ArrayUtil.containsIgnoreCase(sysUsers, userInfo.getUserName())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, userInfo.getUserName()));
            return;
        }
        if (!this.existUser(userInfo, resultT)) {
            String sql = TemplateUtil.rendering(CREATE_USER, userInfo);
            this.exe(sql, resultT);
        }
    }

    @Override
    public void dropUser(UserInfo userInfo, ResultT<String> resultT) {
        if (ArrayUtil.containsIgnoreCase(sysUsers, userInfo.getUserName())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, userInfo.getUserName()));
            return;
        }
        if (this.existUser(userInfo, resultT)) {
            String sql = TemplateUtil.rendering(DROP_USER, userInfo);
            this.exe(sql, resultT);
        }
    }

    @Override
    public boolean existUser(UserInfo userInfo, ResultT<String> resultT) {
        if (databaseType.equals(DatabaseTypesEnum.CASSANDRA)) {
            return this.cassandraUserIn(QUERY_USER, userInfo.getUserName());
        } else {
            String sql = TemplateUtil.rendering(QUERY_USER, userInfo);
            return this.exeQuery(sql);
        }
    }

    @Override
    public void alterPwd(UserInfo userInfo, ResultT<String> resultT) {
        String sql = TemplateUtil.rendering(ALTER_USER_PWD, userInfo);
        this.exe(sql, resultT);
    }

    @Override
    public void alterWhitelist(UserInfo userInfo, ResultT<String> resultT) {
        String sql = TemplateUtil.rendering(ALTER_USER_WHITELIST, userInfo);
        this.exe(sql, resultT);
    }




    /*------------------------------------模式-----------------------------------------*/


    @Override
    public Boolean existSchema(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.format(this.databaseType);
        String sql = TemplateUtil.rendering(QUERY_SCHEMA, authorityVo);
        return this.exeQuery(sql);
    }

    @Override
    public void createSchema(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.format(this.databaseType);
        if (ArrayUtil.containsIgnoreCase(sysUsers, authorityVo.getSchema())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, authorityVo.getSchema()));
            return;
        }
        String sql = TemplateUtil.rendering(CREATE_SCHEMA, authorityVo);
        this.exe(sql, resultT);
        this.grantAnyTable(authorityVo, resultT);
    }

    @Override
    public void dropSchema(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.format(this.databaseType);
        if (ArrayUtil.containsIgnoreCase(sysUsers, authorityVo.getSchema())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, authorityVo.getSchema()));
            return;
        }
        String sql = TemplateUtil.rendering(DROP_SCHEMA, authorityVo);
        this.exe(sql, resultT);
    }

    @Override
    public void grantAnyTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.format(this.databaseType);
        String[] grantArr = authorityVo.getGrantArr();
        for (int i = 0; i < grantArr.length; i++) {
            authorityVo.setGrantStr(grantArr[i]);
            String sql = TemplateUtil.rendering(GRANT_ANY_TABLE, authorityVo);
            this.exe(sql, resultT);
        }
    }


    /*---------------------------------------表------------------------------------------*/


    @Override
    public void grantTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.format(this.databaseType);
        String sql = TemplateUtil.rendering(GRANT_TABLE, authorityVo);
        this.exe(sql, resultT);
    }

    @Override
    public void revokeTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.format(this.databaseType);
        String sql = TemplateUtil.rendering(REVOKE_TABLE, authorityVo);
        this.exe(sql, resultT);
    }

    @Override
    public void dropTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.format(this.databaseType);
        String sql = TemplateUtil.rendering(DROP_TABLE, authorityVo);
        this.exe(sql, resultT);
    }

    @Override
    public void addColumn(ColumnVo columnVo, ResultT resultT) {
        columnVo.format();
        String sql = TemplateUtil.rendering(ALTER_ADD_COLUMN, columnVo);
        this.exe(sql, resultT);
    }

    @Override
    public void dropColumn(ColumnVo columnVo, ResultT resultT) {
        columnVo.format();
        String sql = TemplateUtil.rendering(ALTER_DROP_COLUMN, columnVo);
        this.exe(sql, resultT);
    }

    @Override
    public void renameColumn(ColumnVo columnVo, ResultT resultT) {
        columnVo.format();
        String sql = TemplateUtil.rendering(ALTER_RENAME_COLUMN, columnVo);
        this.exe(sql, resultT);
    }

    @Override
    public void alterColumnAttr(ColumnVo columnVo, ResultT resultT) {
        columnVo.format();
        String sql = TemplateUtil.rendering(ALTER_COLUMN_ATTR, columnVo);
        this.exe(sql, resultT);
    }

    @Override
    public void setDefault(ColumnVo columnVo, ResultT resultT) {
        if (StringUtils.isEmpty(ALTER_COLUMN_SET_DEFAULT)){
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(ALTER_COLUMN_SET_DEFAULT, columnVo);
        this.exe(sql, resultT);
    }

    @Override
    public void dropDefault(ColumnVo columnVo, ResultT resultT) {
        if (StringUtils.isEmpty(ALTER_COLUMN_DROP_DEFAULT)){
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(ALTER_COLUMN_DROP_DEFAULT, columnVo);
        this.exe(sql, resultT);
    }

    @Override
    public void setNotnull(ColumnVo columnVo, ResultT resultT) {
        if (StringUtils.isEmpty(ALTER_COLUMN_SET_NOTNULL)){
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(ALTER_COLUMN_SET_NOTNULL, columnVo);
        this.exe(sql, resultT);
    }

    @Override
    public void dropNotnull(ColumnVo columnVo, ResultT resultT) {
        if (StringUtils.isEmpty(ALTER_COLUMN_DROP_NOTNULL)){
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(ALTER_COLUMN_DROP_NOTNULL, columnVo);
        this.exe(sql, resultT);
    }

    @Override
    public Boolean existTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.format(this.databaseType);
        String sql = TemplateUtil.rendering(QUERY_COLUMN, authorityVo);
        return this.exeQuery(sql);
    }

    @Override
    public void columnInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT) {
        authorityVo.format(this.databaseType);
        String sql = TemplateUtil.rendering(QUERY_COLUMN, authorityVo);
        resultT.setData(this.exeQuery(sql, resultT));
    }

    @Override
    public void indexInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT) {
        authorityVo.format(this.databaseType);
        String sql = TemplateUtil.rendering(QUERY_INDEX, authorityVo);
        resultT.setData(this.exeQuery(sql, resultT));
    }

    @Override
    public void allTables(AuthorityVo authorityVo,  ResultT<List<Map<String, Object>>> resultT) {
        authorityVo.format(this.databaseType);
        String sql = TemplateUtil.rendering(QUERY_TABLES, authorityVo);
        resultT.setData(this.exeQuery(sql, resultT));
    }


}
