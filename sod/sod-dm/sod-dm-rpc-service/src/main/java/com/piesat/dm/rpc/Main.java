package com.piesat.dm.rpc;

import com.piesat.dm.common.constants.Constants;
import com.piesat.dm.core.action.build.DataBuild;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.model.ConnectVo;
import com.piesat.dm.core.model.SelectVo;
import com.piesat.util.ResultT;

public class Main {
    public static void main(String[] args) {
        ResultT r = new ResultT();
        ConnectVo connectVo = new ConnectVo();
        connectVo.setDatabaseType(DatabaseTypesEnum.XUGU);
        connectVo.setUrl("jdbc:xugu://10.20.64.167:31380/BABJ_SMDB?char_set=utf8&recv_mode=0");
        String url = connectVo.getUrl();
        int i = url.indexOf(Constants.QUESTION_MARK);
        int i1 = url.indexOf(Constants.BACKSLASH, url.lastIndexOf(Constants.BACKSLASH_DUBLE) + 2);
        String databaseName = url.substring(i1 + 1, i);
        url.replace(databaseName,Constants.SYSTEM);
        connectVo.setUrl(url);
        connectVo.setUserName(Constants.SYSDBA);
        connectVo.setPassWord(Constants.SYSDBA);
        connectVo.setPid("SMDB");
        SelectVo s = new SelectVo();
        s.setSchema("USR_SOD");
        s.setDatabaseName(databaseName);
        new DataBuild()
                .init(connectVo, r)
                .statisticalSpace(s, r)
                .close();
        System.out.println(r.getData());
    }
}
