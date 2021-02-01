package com.piesat.dm.core.action.exc.impl.cus;

import cn.hutool.core.util.ArrayUtil;
import com.piesat.dm.common.constants.ConstantsMsg;
import com.piesat.dm.common.util.TemplateUtil;
import com.piesat.dm.core.action.exc.impl.Exc;
import com.piesat.dm.core.action.impl.CassandraDatabaseImpl;
import com.piesat.dm.core.model.AuthorityVo;
import com.piesat.dm.core.model.UserInfo;
import com.piesat.util.ResultT;

/**
 * @author cwh
 * @program: sod
 * @description:
 * @date 2021年 01月27日 17:48:38
 */
public class CassandraExc extends Exc {

    @Override
    public boolean existUser(UserInfo userInfo, ResultT<String> resultT) {
        return ((CassandraDatabaseImpl) this.bi).cassandraUserIn(T.QUERY_USER, userInfo.getUserName());
    }


}
