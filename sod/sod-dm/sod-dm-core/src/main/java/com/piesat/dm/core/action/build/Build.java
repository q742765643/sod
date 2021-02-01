package com.piesat.dm.core.action.build;

import com.piesat.dm.core.action.Action;
import com.piesat.dm.core.action.BaseAction;
import com.piesat.dm.core.action.exc.abs.ExcAbs;
import com.piesat.dm.core.action.impl.abs.BaseAbs;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.util.ResultT;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月27日 14:51:03
 */
public class Build implements Action {
    private ExcAbs ea;

    @Override
    public Action init(ConnectVo c, DatabaseTypesEnum databaseType, ResultT r) {
        this.ea = databaseType.init(c, r);
        return null;
    }
}
