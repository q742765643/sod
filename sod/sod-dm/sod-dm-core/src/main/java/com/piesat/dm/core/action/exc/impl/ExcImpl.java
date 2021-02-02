package com.piesat.dm.core.action.exc.impl;

import cn.hutool.core.util.ArrayUtil;
import com.piesat.common.utils.StringUtils;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.common.util.TemplateUtil;
import com.piesat.dm.core.action.exc.Exc;
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
public class ExcImpl extends ExcAbs {

    @Override
    public ExcAbs init(BaseAbs ba, SqlTemplateComm t) {
        this.T = t;
        this.ba = ba;
        return this;
    }

    @Override
    public void close() {
        this.ba.close();
    }

    @Override
    public Exc exc(String sql, ResultT<String> resultT) {
        this.ba.exe(sql,resultT);
        return this;
    }

    @Override
    public Exc doCreateUser(UserInfo userInfo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        if (ArrayUtil.containsIgnoreCase(sysUsers, userInfo.getUserName())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, userInfo.getUserName()));
            return this;
        }
        if (!this.existUser(userInfo, resultT)) {
            String sql = TemplateUtil.rendering(T.CREATE_USER, userInfo);
            this.ba.exe(sql, resultT);
        }
        return this;
    }

    @Override
    public Exc dropUser(UserInfo userInfo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        if (ArrayUtil.containsIgnoreCase(sysUsers, userInfo.getUserName())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, userInfo.getUserName()));
            return this;
        }
        if (this.existUser(userInfo, resultT)) {
            String sql = TemplateUtil.rendering(T.DROP_USER, userInfo);
            this.ba.exe(sql, resultT);
        }
        return this;
    }

    @Override
    public boolean existUser(UserInfo userInfo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return false;
        }
        String sql = TemplateUtil.rendering(T.QUERY_USER, userInfo);
        return this.ba.exeQuery(sql);
    }

    @Override
    public Exc alterPwd(UserInfo userInfo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        String sql = TemplateUtil.rendering(T.ALTER_USER_PWD, userInfo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc alterWhitelist(UserInfo userInfo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        String sql = TemplateUtil.rendering(T.ALTER_USER_WHITELIST, userInfo);
        this.ba.exe(sql, resultT);
        return this;
    }




    /*------------------------------------模式-----------------------------------------*/


    @Override
    public Boolean existSchema(AuthorityVo authorityVo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return false;
        }
        authorityVo.lower(this.ba.lower);
        String sql = TemplateUtil.rendering(T.QUERY_SCHEMA, authorityVo);
        return this.ba.exeQuery(sql);
    }

    @Override
    public Exc createSchema(AuthorityVo authorityVo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        authorityVo.lower(this.ba.lower);
        if (ArrayUtil.containsIgnoreCase(sysUsers, authorityVo.getSchema())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, authorityVo.getSchema()));
            return this;
        }
        String sql = TemplateUtil.rendering(T.CREATE_SCHEMA, authorityVo);
        this.ba.exe(sql, resultT);
        this.grantAnyTable(authorityVo, resultT);
        return this;
    }

    @Override
    public Exc dropSchema(AuthorityVo authorityVo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        authorityVo.lower(this.ba.lower);
        if (ArrayUtil.containsIgnoreCase(sysUsers, authorityVo.getSchema())) {
            resultT.setErrorMessage(String.format(ConstantsMsg.MSG1, authorityVo.getSchema()));
            return this;
        }
        String sql = TemplateUtil.rendering(T.DROP_SCHEMA, authorityVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc grantAnyTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        authorityVo.lower(this.ba.lower);
        String[] grantArr = authorityVo.getGrantArr();
        for (int i = 0; i < grantArr.length; i++) {
            authorityVo.setGrantStr(grantArr[i]);
            String sql = TemplateUtil.rendering(T.GRANT_ANY_TABLE, authorityVo);
            this.ba.exe(sql, resultT);
        }
        return this;
    }


    /*---------------------------------------表------------------------------------------*/


    @Override
    public Exc grantTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        authorityVo.lower(this.ba.lower);
        String sql = TemplateUtil.rendering(T.GRANT_TABLE, authorityVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc revokeTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        authorityVo.lower(this.ba.lower);
        String sql = TemplateUtil.rendering(T.REVOKE_TABLE, authorityVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc dropTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        authorityVo.lower(this.ba.lower);
        String sql = TemplateUtil.rendering(T.DROP_TABLE, authorityVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc addColumn(ColumnVo columnVo, ResultT resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_ADD_COLUMN, columnVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc dropColumn(ColumnVo columnVo, ResultT resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_DROP_COLUMN, columnVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc renameColumn(ColumnVo columnVo, ResultT resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_RENAME_COLUMN, columnVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc alterColumnAttr(ColumnVo columnVo, ResultT resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_ATTR, columnVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc setDefault(ColumnVo columnVo, ResultT resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        if (StringUtils.isEmpty(T.ALTER_COLUMN_SET_DEFAULT)) {
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return this;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_SET_DEFAULT, columnVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc dropDefault(ColumnVo columnVo, ResultT resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        if (StringUtils.isEmpty(T.ALTER_COLUMN_DROP_DEFAULT)) {
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return this;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_DROP_DEFAULT, columnVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc setNotnull(ColumnVo columnVo, ResultT resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        if (StringUtils.isEmpty(T.ALTER_COLUMN_SET_NOTNULL)) {
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return this;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_SET_NOTNULL, columnVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Exc dropNotnull(ColumnVo columnVo, ResultT resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        if (StringUtils.isEmpty(T.ALTER_COLUMN_DROP_NOTNULL)) {
            resultT.setErrorMessage(ConstantsMsg.MSG14);
            return this;
        }
        columnVo.format();
        String sql = TemplateUtil.rendering(T.ALTER_COLUMN_DROP_NOTNULL, columnVo);
        this.ba.exe(sql, resultT);
        return this;
    }

    @Override
    public Boolean existTable(AuthorityVo authorityVo, ResultT<String> resultT) {
        if (!resultT.isSuccess()) {
            return false;
        }
        authorityVo.lower(this.ba.lower);
        String sql = TemplateUtil.rendering(T.QUERY_COLUMN, authorityVo);
        return this.ba.exeQuery(sql);
    }

    @Override
    public Exc columnInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        authorityVo.lower(this.ba.lower);
        String sql = TemplateUtil.rendering(T.QUERY_COLUMN, authorityVo);
        resultT.setData(this.ba.exeQuery(sql, resultT));
        return this;
    }

    @Override
    public Exc indexInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        authorityVo.lower(this.ba.lower);
        String sql = TemplateUtil.rendering(T.QUERY_INDEX, authorityVo);
        resultT.setData(this.ba.exeQuery(sql, resultT));
        return this;
    }

    @Override
    public Exc allTables(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT) {
        if (!resultT.isSuccess()) {
            return this;
        }
        authorityVo.lower(this.ba.lower);
        String sql = TemplateUtil.rendering(T.QUERY_TABLES, authorityVo);
        resultT.setData(this.ba.exeQuery(sql, resultT));
        return this;
    }
}
