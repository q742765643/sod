package com.piesat.dm.core.action.exc.abs;

import com.piesat.dm.core.action.impl.abs.BaseAbs;
import com.piesat.dm.core.constants.Constants;
import com.piesat.dm.core.model.AuthorityVo;
import com.piesat.dm.core.model.ColumnVo;
import com.piesat.dm.core.model.UserInfo;
import com.piesat.dm.core.parser.ManagerUser;
import com.piesat.dm.core.template.SqlTemplateComm;
import com.piesat.util.ResultT;

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月27日 17:05:07
 */
public abstract class ExcAbs {

    public String[] sysUsers = ManagerUser.sysUser.split(Constants.COMMA);

    public SqlTemplateComm T = new SqlTemplateComm();

    public BaseAbs bi;

    /**
     * 初始化
     * @param bi
     * @param t
     * @return
     */
    public abstract ExcAbs init(BaseAbs bi, SqlTemplateComm t);

    /**
     * 创建用户
     *
     * @param userInfo
     * @param resultT
     */
    public abstract void doCreateUser(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 删除用户
     *
     * @param userInfo
     * @param resultT
     */
    public abstract void dropUser(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 是否存在用户
     *
     * @param userInfo
     * @param resultT
     * @return
     */
    public abstract boolean existUser(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 修改密码
     *
     * @param userInfo
     * @param resultT
     */
    public abstract void alterPwd(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 修改白名单
     *
     * @param userInfo
     * @param resultT
     */
    public abstract void alterWhitelist(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 是否存在模式
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    public abstract Boolean existSchema(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 创建模式
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void createSchema(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 删除模式
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void dropSchema(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 模式表赋权
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void grantAnyTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 添加表权限
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void grantTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 删除表权限
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void revokeTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 删除表
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void dropTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 数据库新增字段
     *
     * @param columnVo
     * @param resultT
     */
    public abstract void addColumn(ColumnVo columnVo, ResultT resultT);

    /**
     * 删除数据库字段
     *
     * @param columnVo
     * @param resultT
     */
    public abstract void dropColumn(ColumnVo columnVo, ResultT resultT);

    /**
     * 字段名重命名
     *
     * @param columnVo
     * @param resultT
     */
    public abstract void renameColumn(ColumnVo columnVo, ResultT resultT);

    /**
     * 修改字段信息
     *
     * @param columnVo
     * @param resultT
     */
    public abstract void alterColumnAttr(ColumnVo columnVo, ResultT resultT);


    /**
     * 设置默认值
     *
     * @param columnVo
     * @param resultT
     */
    public abstract void setDefault(ColumnVo columnVo, ResultT resultT);

    /**
     * 删除默认值
     *
     * @param columnVo
     * @param resultT
     */
    public abstract void dropDefault(ColumnVo columnVo, ResultT resultT);

    /**
     * 设置非空
     *
     * @param columnVo
     * @param resultT
     */
    public abstract void setNotnull(ColumnVo columnVo, ResultT resultT);

    /**
     * 删除非空
     *
     * @param columnVo
     * @param resultT
     */
    public abstract void dropNotnull(ColumnVo columnVo, ResultT resultT);


    /**
     * 表是否存在
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    public abstract Boolean existTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 字段信息
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void columnInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT);

    /**
     * 索引信息
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void indexInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT);

    /**
     * 查询所有表
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void allTables(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT);
}
