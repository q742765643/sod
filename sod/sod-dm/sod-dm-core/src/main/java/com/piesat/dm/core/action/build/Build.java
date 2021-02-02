package com.piesat.dm.core.action.build;

import com.piesat.dm.core.action.Action;
import com.piesat.dm.core.action.exc.abs.ExcAbs;
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
    public Build init(ConnectVo c, ResultT r) {
        this.ea = c.build(r);
        return this;
    }

    @Override
    public void close() {
        this.ea.close();
    }

    public ExcAbs getExc(){
        return this.ea;
    }
}
