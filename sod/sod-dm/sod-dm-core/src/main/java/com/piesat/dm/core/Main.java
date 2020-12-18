package com.piesat.dm.core;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.piesat.dm.core.datasource.CommDataSource;
import com.piesat.dm.core.enums.DatabaseTypesEnum;
import com.piesat.dm.core.factory.AuzDatabase;
import com.piesat.dm.core.factory.AuzFactory;
import com.piesat.dm.core.model.*;
import com.piesat.util.ResultT;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @date 2020年 12月04日 17:59:42
 */
public class Main {
    public static void main(String[] args) throws SQLException {


        ResultT r = new ResultT();


        TableVo t = new TableVo();

        IndexVo i = new IndexVo();
        t.setSchema("USR_SOD");
        t.setTableName("TEST1");
        t.setPartiColumn("D_DATETIME");
        List<ColumnVo> l = new ArrayList<>();
        ColumnVo c2 = new ColumnVo();
        c2.setColumnName("D_DATA_ID");
        c2.setIsNull(true);
//        c2.setPrecision(6);
        c2.setLength(8);
        c2.setComment("测试");
        c2.setType("VARCHAR");
        c2.setDef("hahahahahaha");
        c2.format();
        l.add(c2);
        c2 = new ColumnVo();
        c2.setColumnName("D_DATETIME");
        c2.setIsNull(false);
        c2.setComment("测试");
        c2.setType("DATETIME");
//        c2.setDef("HHHHHHH");
        c2.format();
        l.add(c2);
        c2 = new ColumnVo();
        c2.setColumnName("D_IYMDHM");
        c2.setIsNull(true);
        c2.setComment("测试");
        c2.setType("DATETIME");
//        c2.setDef("HHHHHHH");
        c2.format();
        l.add(c2);
        List<IndexVo> l1 = new ArrayList<>();
        i.setIndexType1("UNIQUE");
        i.setIndexType2("BTREE");
        i.setIndexName("CESHI");
        i.setIndexComment("D_IYMDHM,D_DATETIME");
        l1.add(i);
        t.setColumnVos(l);
        t.setIndexVos(l1);
        AuzFactory.createTableSql(t,r);
        System.out.println(r.getData());

        AuzFactory.insertTableSql(t,r);
        System.out.println(r.getData());

        AuzFactory.queryTableSql(t,r);
        System.out.println(r.getData());

            Map<String,Boolean> map = new HashMap<>();
            Map<String,Object> m = new HashMap<>();
            m.put("status",true);
            map.put("HADB",true);
            map.put("STDB",true);
            map.put("BFDB",false);
        String s1 = JSON.toJSONString(map);
        JSONObject jsonObject = JSON.parseObject("{\"STDB\":true,\"HADB\":true,\"FIDB\":true,\"RADB\":true}");
//        System.out.println(jsonObject);
//        System.out.println(s1);
        if (true) {
            return;
        }
        ConnectVo c = new ConnectVo();
//        c.setUrl("jdbc:xugu://10.20.63.196:5138/BABJ_STDB?ips=10.20.63.197,10.20.63.198,10.20.63.210,10.20.63.212,10.20.63.214&char_set=utf8&recv_mode=2");
//        c.setPort(5138);
//        c.setClassName("com.xugu.cloudjdbc.Driver");
//        c.setUserName("usr_manager");
//        c.setPassWord("manager_123");


        c.setUrl("jdbc:gbase://10.20.64.29:5258/usr_sod?useOldAliasMetadataBehavior=true&rewriteBatchedStatements=true&connectTimeout=0&hostList=10.20.64.29,10.20.64.30,10.20.64.31&failoverEnable=true");
        c.setPort(5258);
        c.setClassName("com.gbase.jdbc.Driver");
        c.setUserName("usr_manager");
        c.setPassWord("Manager_123");

        try {
//            c.setIp("10.40.128.25");
//            c.setPort(9042);
//            c.setUserName("usr_manager");
//            c.setPassWord("Cass@admin2019");
//            Session radb = CassandraSource.getConnection("RADB", c);
            Connection stdb = CommDataSource.getConnection("STDB", c);

            AuzDatabase a = new AuzDatabase(DatabaseTypesEnum.GBASE, stdb);

            SelectVo s = new SelectVo();
            s.setSchema("usr_sod");
            s.setTableName("SURF_WEA_CHN_MAIN_HOR_TAB");
//            AuthorityVo av = new AuthorityVo();
//            av.setSchema("usr_sod");
//            av.setTableName("surf_wea_chn_mul_min_tab");

//            a.indexInfo(av, r);
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            System.out.println(JSON.toJSONString(r.getData()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
