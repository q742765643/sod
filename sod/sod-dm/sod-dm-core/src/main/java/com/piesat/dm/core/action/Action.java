package com.piesat.dm.core.action;

import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.util.ResultT;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月26日 18:36:31
 */
public interface Action {
    /**
     * 初始化
     * @param c
     * @param databaseType
     * @param r
     * @return
     */
    Action init(ConnectVo c, DatabaseTypesEnum databaseType, ResultT r);
}
