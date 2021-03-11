package com.piesat.dm.core.action;

import com.piesat.dm.core.action.impl.abs.BaseAbs;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.util.ResultT;

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月26日 16:56:13
 */
public interface BaseAction {

    /**
     * 初始化
     *
     * @param c
     * @param r
     * @return
     */
    BaseAbs init(ConnectVo c, ResultT r);

    /**
     * 执行sql
     *
     * @param sql
     * @param r
     */
    void exe(String sql, ResultT r);

    /**
     * 查询sql，返回Boolean,查询是否有返回
     *
     * @param sql
     * @return
     */
    Boolean exeQuery(String sql);

    /**
     * 查询返回map数组
     *
     * @param sql
     * @param r
     * @return
     */
    List<Map<String, Object>> exeQuery(String sql, ResultT r);

    /**
     * 查询返回单条数据
     *
     * @param sql
     * @param r
     * @return
     */
    Map<String, Object> exeQueryOne(String sql, ResultT r);

    /**
     * 关闭连接
     */
    void close();
}
