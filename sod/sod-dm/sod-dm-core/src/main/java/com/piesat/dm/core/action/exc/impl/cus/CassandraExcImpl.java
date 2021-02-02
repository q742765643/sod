package com.piesat.dm.core.action.exc.impl.cus;

import com.piesat.dm.core.action.exc.impl.ExcImpl;
import com.piesat.dm.core.action.impl.CassandraDatabaseImpl;
import com.piesat.dm.core.model.UserInfo;
import com.piesat.util.ResultT;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月27日 17:48:38
 */
public class CassandraExcImpl extends ExcImpl {

    @Override
    public boolean existUser(UserInfo userInfo, ResultT<String> resultT) {
        return ((CassandraDatabaseImpl) this.ba).cassandraUserIn(T.QUERY_USER, userInfo.getUserName());
    }

}
