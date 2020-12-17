package com.piesat.dm.core.factory;

import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.AuthorityVo;
import com.piesat.dm.core.model.TableVo;
import com.piesat.dm.core.model.UserInfo;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * 数据库授权
 *
 * @author cwh
 * @date 2020年 12月07日 16:24:49
 */
@Slf4j
public abstract class AuzBase extends Actuator {


    public AuzBase(DatabaseTypesEnum databaseType, Object con) {
        super(databaseType, con);
    }

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
     * 字段信息
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void columnInfo(AuthorityVo authorityVo, ResultT<List> resultT);

    /**
     * 索引信息
     *
     * @param authorityVo
     * @param resultT
     */
    public abstract void indexInfo(AuthorityVo authorityVo, ResultT<List> resultT);

}
