package com.piesat.dm.core.api;

import com.piesat.dm.core.model.Column;
import com.piesat.util.ResultT;

import java.util.List;

/**
 * 数据库管理
 *
 * @author cwh
 * @date 2019年 12月27日 18:33:25
 */
public interface DatabaseDcl {
    /**
     * 关闭连接
     */
    void closeConnect();

    /**
     * 自动提交
     *
     * @param flag
     */
    void autoCommit(Boolean flag);

    /**
     * 手动提交
     */
    void commit();

    /**
     * 回滚
     */
    void rollback();

    /**
     * 新增用户
     *
     * @param identifier 用户标识
     * @param password   密码
     */
    void addUser(String identifier, String password,String[] ips) throws Exception;

    /**
     * 删除用户
     *
     * @param identifier 用户标识
     */
    void deleteUser(String identifier, String ip) throws Exception;

    void deleteUser(String identifier) throws Exception;

    /**
     * 增加白名单用户
     *
     * @param identifier 用户标识
     * @param resource   数据空间（资源）
     * @param ips         白名单ip
     * @param type       ip或ip段，（0：IP，1：IP段）
     */
    void addEnable(String identifier, String resource, List<String> ips, int type) throws Exception;

    /**
     * 删除白名单用户
     *
     * @param identifier 用户标识
     * @param resource   数据空间（资源）
     * @param ips         白名单ip
     * @param type       ip或ip段，（0：IP，1：IP段）
     */
    void deleteEnable(String identifier, String resource, List<String> ips, int type) throws Exception;

    /**
     * 增加权限
     *
     * @param select 读权限
     */
    void addPermissions(Boolean select, String resource, String tableName, String identifier, String password, List<String> ips)  throws Exception;

    /**
     * 删除权限
     *
     * @param select 读权限
     */
    void deletePermissions(String[] permissions, String resource, String tableName, String identifier, String password, List<String> ips) throws Exception;

    /**
     * 新增数据库实例
     *
     * @param schemaName
     * @param dataBaseUser
     * @param dataAuthor   数据权限
     * @param creatAuthor  建表权限
     * @param dropAuthor   删表权限
     */
    void createSchemas(String schemaName, String dataBaseUser, String password, boolean dataAuthor, boolean creatAuthor, boolean dropAuthor, List<String> ips) throws Exception;

    /**
     * 删除数据库实例
     *
     * @param schemaName
     */
    void dropSchemas(String schemaName) throws Exception;

    /**
     * 根据sql创建表结构
     * @param sql 建表sql
     * @param tableName 表名
     * @param deleteOld 是否删除旧表
     * @return
     */
    ResultT createTable(String sql, String tableName, Boolean deleteOld) throws Exception;

    /**
     * 查询表结构是否存在
     * @param schema 实例名
     * @param tableName 表名
     * @return
     */
    ResultT existTable(String schema, String tableName) throws Exception;

    /**
     * 修改数据库密码
     * @param dataBaseUser
     * @param newPassword
     * @return
     */
    ResultT updateAccount(String dataBaseUser, String newPassword) throws Exception;

    ResultT queryData(String schema,String tableName, List<String> column,int row) throws Exception;

    /**
     * 查询实例下所有表名
     * @param schema
     * @return
     * @throws Exception
     */
    ResultT queryAllTableName(String schema) throws Exception;

    /**
     *  查询字段信息
     * @param schema
     * @param tableName
     * @return
     * @throws Exception
     */
    ResultT queryAllColumnInfo(String schema, String tableName) throws Exception;

    /**
     * 查询索引和分库分表信息
     * @param schema
     * @param tableName
     * @return
     * @throws Exception
     */
    ResultT queryAllIndexAndShardingInfo(String schema, String tableName) throws Exception;

    ResultT updateColumn(String schema, String tableName, Column oldColumn, Column newColumn);

    void bindIp(String identifier,String[] ips) throws Exception;

    /**
     * 查询表中记录条数
     * @param schema
     * @param tableName
     * @return
     * @throws Exception
     */
    String queryRecordNum(String schema, String tableName) throws Exception;


    /**
     * 获取最小时间
     * @param schema
     * @param tableName
     * @param timeColumnName
     * @return
     * @throws Exception
     */
    String queryMinTime(String schema, String tableName, String timeColumnName) throws Exception;

    /**
     * 获取最大时间
     * @param schema
     * @param tableName
     * @param timeColumnName
     * @return
     * @throws Exception
     */
    String queryMaxTime(String schema, String tableName, String timeColumnName) throws Exception;


    /**
     *
     * @param schema
     * @param tableName
     * @param timeColumnName
     * @param beginTime
     * @param endTime
     * @return
     * @throws Exception
     */
    String queryIncreCount(String schema, String tableName, String timeColumnName,String beginTime,String endTime) throws Exception;

}
