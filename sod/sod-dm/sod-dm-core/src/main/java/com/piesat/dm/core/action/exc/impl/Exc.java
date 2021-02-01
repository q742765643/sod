package com.piesat.dm.core.action.exc.impl;

import cn.hutool.core.util.ArrayUtil;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.common.util.TemplateUtil;
import com.piesat.dm.core.action.exc.abs.ExcAbs;
import com.piesat.dm.core.action.impl.abs.BaseAbs;
import com.piesat.dm.core.model.AuthorityVo;
import com.piesat.dm.core.model.ColumnVo;
import com.piesat.dm.core.model.UserInfo;
import com.piesat.dm.core.template.SqlTemplateComm;
import com.piesat.util.ResultT;

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月27日 17:27:40
 */
public class Exc extends ExcAbs {

    @Override
    public ExcAbs init(BaseAbs da, SqlTemplateComm t) {
        this.T = t;
        this.bi = da;
        return this;
    }

    @Override
    public void doCreateUser(UserInfo userInfo, ResultT<String> resultT) {
        if (ArrayUtil.containsIgnoreCase(sysUsers, userInfo.getUserName())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, userInfo.getUserName()));
            return;
        }
        if (!this.existUser(userInfo, resultT)) {
            String sql = TemplateUtil.rendering(T.CREATE_USER, userInfo);
            this.bi.exe(sql, resultT);
        }
    }

    @Override
    public void dropUser(UserInfo userInfo, ResultT<String> resultT) {
        if (ArrayUtil.containsIgnoreCase(sysUsers, userInfo.getUserName())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, userInfo.getUserName()));
            return;
        }
        if (this.existUser(userInfo, resultT)) {
            String sql = TemplateUtil.rendering(T.DROP_USER, userInfo);
            this.bi.exe(sql, resultT);
        }
    }

    @Override
    public boolean existUser(UserInfo userInfo, ResultT<String> resultT) {
        String sql = TemplateUtil.rendering(T.QUERY_USER, userInfo);
        return this.bi.exeQuery(sql);
    }

    @Override
    public void alterPwd(UserInfo userInfo, ResultT<String> resultT) {
        String sql = TemplateUtil.rendering(T.ALTER_USER_PWD, userInfo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void alterWhitelist(UserInfo userInfo, ResultT<String> resultT) {
        String sql = TemplateUtil.rendering(T.ALTER_USER_WHITELIST, userInfo);
        this.bi.exe(sql, resultT);
    }




    /*------------------------------------模式-----------------------------------------*/


    @Override
    public Boolean existSchema(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.lower(this.bi.lower);
        String sql = TemplateUtil.rendering(T.QUERY_SCHEMA, authorityVo);
        return this.bi.exeQuery(sql);
    }

    @Override
    public void createSchema(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.lower(this.bi.lower);
        if (ArrayUtil.containsIgnoreCase(sysUsers, authorityVo.getSchema())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, authorityVo.getSchema()));
            return;
        }
        String sql = TemplateUtil.rendering(T.CREATE_SCHEMA, authorityVo);
        this.bi.exe(sql, resultT);
        this.grantAnyTable(authorityVo, resultT);
    }

    @Override
    public void dropSchema(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.lower(this.bi.lower);
        if (ArrayUtil.containsIgnoreCase(sysUsers, authorityVo.getSchema())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, authorityVo.getSchema()));
            return;
        }
        String sql = TemplateUtil.rendering(T.DROP_SCHEMA, authorityVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void grantAnyTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.lower(this.bi.lower);
        String[] grantArr = authorityVo.getGrantArr();
        for (int i = 0; i < grantArr.length; i++) {
            authorityVo.setGrantStr(grantArr[i]);
            String sql = TemplateUtil.rendering(T.GRANT_ANY_TABLE, authorityVo);
            this.bi.exe(sql, resultT);
        }
    }


    /*---------------------------------------表------------------------------------------*/


    @Override
    public void grantTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.lower(this.bi.lower);
        String sql = TemplateUtil.rendering(T.GRANT_TABLE, authorityVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void revokeTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.lower(this.bi.lower);
        String sql = TemplateUtil.rendering(T.REVOKE_TABLE, authorityVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void dropTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.lower(this.bi.lower);
        String sql = TemplateUtil.rendering(T.DROP_TABLE, authorityVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void addColumn(ColumnVo columnVo, ResultT resultT) {
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_ADD_COLUMN, columnVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void dropColumn(ColumnVo columnVo, ResultT resultT) {
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_DROP_COLUMN, columnVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void renameColumn(ColumnVo columnVo, ResultT resultT) {
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_RENAME_COLUMN, columnVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void alterColumnAttr(ColumnVo columnVo, ResultT resultT) {
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_ATTR, columnVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void setDefault(ColumnVo columnVo, ResultT resultT) {
        if (StringUtils.isEmpty(T.ALTER_COLUMN_SET_DEFAULT)) {
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_SET_DEFAULT, columnVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void dropDefault(ColumnVo columnVo, ResultT resultT) {
        if (StringUtils.isEmpty(T.ALTER_COLUMN_DROP_DEFAULT)) {
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_DROP_DEFAULT, columnVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void setNotnull(ColumnVo columnVo, ResultT resultT) {
        if (StringUtils.isEmpty(T.ALTER_COLUMN_SET_NOTNULL)) {
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_SET_NOTNULL, columnVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public void dropNotnull(ColumnVo columnVo, ResultT resultT) {
        if (StringUtils.isEmpty(T.ALTER_COLUMN_DROP_NOTNULL)) {
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_DROP_NOTNULL, columnVo);
        this.bi.exe(sql, resultT);
    }

    @Override
    public Boolean existTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        authorityVo.lower(this.bi.lower);
        String sql = TemplateUtil.rendering(T.QUERY_COLUMN, authorityVo);
        return this.bi.exeQuery(sql);
    }

    @Override
    public void columnInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT) {
        authorityVo.lower(this.bi.lower);
        String sql = TemplateUtil.rendering(T.QUERY_COLUMN, authorityVo);
        resultT.setData(this.bi.exeQuery(sql, resultT));
    }

    @Override
    public void indexInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT) {
        authorityVo.lower(this.bi.lower);
        String sql = TemplateUtil.rendering(T.QUERY_INDEX, authorityVo);
        resultT.setData(this.bi.exeQuery(sql, resultT));
    }

    @Override
    public void allTables(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT) {
        authorityVo.lower(this.bi.lower);
        String sql = TemplateUtil.rendering(T.QUERY_TABLES, authorityVo);
        resultT.setData(this.bi.exeQuery(sql, resultT));
    }


}
