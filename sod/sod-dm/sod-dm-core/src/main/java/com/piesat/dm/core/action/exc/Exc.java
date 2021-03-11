package com.piesat.dm.core.action.exc;

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
 * @description: 数据库操作接口
 * @date 2021年 02月02日 13:40:15
 */
public interface Exc {
    /**
     * 初始化
     *
     * @param bi 数据库连接
     * @param t  模板信息
     * @return
     */
    ExcAbs init(BaseAbs bi, SqlTemplateComm t);

    /**
     * 关闭连接
     */
    void close();

    Exc exc(String sql, ResultT<String> resultT);

    /**
     * 创建用户
     *
     * @param userInfo 用户信息
     * @param resultT  返回结果
     * @return
     */
    Exc doCreateUser(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 删除用户
     *
     * @param userInfo 用户信息
     * @param resultT  返回结果
     * @return
     */
    Exc dropUser(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 是否存在用户
     *
     * @param userInfo 用户信息
     * @param resultT  返回结果
     * @return
     */
    boolean existUser(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 修改密码
     *
     * @param userInfo
     * @param resultT
     * @return
     */
    Exc alterPwd(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 修改白名单
     *
     * @param userInfo
     * @param resultT
     * @return
     */
    Exc alterWhitelist(UserInfo userInfo, ResultT<String> resultT);

    /**
     * 是否存在模式
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Boolean existSchema(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 创建模式
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Exc createSchema(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 删除模式
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Exc dropSchema(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 模式表赋权
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Exc grantAnyTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 添加表权限
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Exc grantTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 删除表权限
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Exc revokeTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 删除表
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Exc dropTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 数据库新增字段
     *
     * @param columnVo
     * @param resultT
     * @return
     */
    Exc addColumn(ColumnVo columnVo, ResultT resultT);

    /**
     * 删除数据库字段
     *
     * @param columnVo
     * @param resultT
     * @return
     */
    Exc dropColumn(ColumnVo columnVo, ResultT resultT);

    /**
     * 字段名重命名
     *
     * @param columnVo
     * @param resultT
     * @return
     */
    Exc renameColumn(ColumnVo columnVo, ResultT resultT);

    /**
     * 修改字段信息
     *
     * @param columnVo
     * @param resultT
     * @return
     */
    Exc alterColumnAttr(ColumnVo columnVo, ResultT resultT);


    /**
     * 设置默认值
     *
     * @param columnVo
     * @param resultT
     * @return
     */
    Exc setDefault(ColumnVo columnVo, ResultT resultT);

    /**
     * 删除默认值
     *
     * @param columnVo
     * @param resultT
     * @return
     */
    Exc dropDefault(ColumnVo columnVo, ResultT resultT);

    /**
     * 设置非空
     *
     * @param columnVo
     * @param resultT
     * @return
     */
    Exc setNotnull(ColumnVo columnVo, ResultT resultT);

    /**
     * 删除非空
     *
     * @param columnVo
     * @param resultT
     * @return
     */
    Exc dropNotnull(ColumnVo columnVo, ResultT resultT);


    /**
     * 表是否存在
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Boolean existTable(AuthorityVo authorityVo, ResultT<String> resultT);

    /**
     * 字段信息
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Exc columnInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT);

    /**
     * 索引信息
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Exc indexInfo(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT);

    /**
     * 查询所有表
     *
     * @param authorityVo
     * @param resultT
     * @return
     */
    Exc allTables(AuthorityVo authorityVo, ResultT<List<Map<String, Object>>> resultT);
}
