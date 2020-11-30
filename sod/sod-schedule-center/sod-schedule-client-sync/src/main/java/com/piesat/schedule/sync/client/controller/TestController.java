package com.piesat.schedule.sync.client.controller;

import cn.hutool.core.date.DateTime;
import com.piesat.schedule.entity.synctofile.SyncToFileEntity;
import com.piesat.schedule.entity.syncudtopc.SyncUdToPcEntity;
import com.piesat.schedule.sync.client.handler.synctofile.SyncToFileHandler;
import com.piesat.schedule.sync.client.handler.syncudtopc.SyncUdToPcHandler;
import com.piesat.util.ResultT;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-25 20:52
 **/
@Api(value = "同步测试", tags = {"同步测试"})
@RestController
public class TestController {
    @Value("${spring.datasource.password}")
    private String pwd;
    @Value("${spring.redis.password}")
    private String redis;

    @Autowired
    private SyncToFileHandler syncToFileHandler;
    @Autowired
    private SyncUdToPcHandler syncUdToPcHandler;

    @ApiOperation(value = "同步测试接口", notes = "同步测试接口")
    @PostMapping("/test")
    public ResultT test() {

        LocalDateTime dateTime = LocalDateTime.now().plusHours(-1);
        Date from = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());


        ResultT<Map<String, String>> resultT = new ResultT<>();
        Map<String, String> map = new HashMap<>();
        map.put("redis", redis);
        map.put("pwd", pwd);
        resultT.setData(map);

//        SyncToFileEntity sf = new SyncToFileEntity();
//        sf.setBufferTime(10000L);
//        sf.setDatabaseId("cfd760e01c02477e9d658aef14871dfb");
//        sf.setParentId("STDB");
//        sf.setDatabaseType("xugu");
//        sf.setDataClassId("A.0012.0001.M002");
//        sf.setDdataId("A.0012.0001.S001");
//        sf.setProfileName("中国地面逐小时数据");
//        sf.setTableName("USR_SOD.SURF_WEA_CHN_MUL_HOR_TAB");
//        sf.setTimeColumn("D_DATETIME");
//        sf.setFtpIp("10.40.17.38");
//        sf.setFtpPort(22);
//        sf.setFtpUser("sod");
//        sf.setFtpPwd("sod@2019");
//        sf.setSaveDirectory("/space/cmadaas/sod/sync_test/");
//        sf.setTransferType("local");
//        sf.setLastTime(from);
//        ResultT r = new ResultT();
//        syncToFileHandler.execute(sf,r);
//        r.setMsg(r.getProcessMsg().toString());


        SyncUdToPcEntity se = new SyncUdToPcEntity();
        se.setBufferTime(10000L);
        se.setDatabaseId("06eadd4476b94b36985e1460de268cbe");
        se.setParentId("FIDB");
        se.setDatabaseType("xugu");
        se.setDataClassId("A.0012.0001.M002");
        se.setDdataId("A.0012.0001.S001");
        se.setProfileName("日本天气模式产品-低分辨率-北半球_预报产品");
        se.setTableName("USR_SOD.NAFP_RJTD_FOR_FTM_FILE_TAB");
        se.setSiteColumn("V_FILE_NAME_SOURCE");
        se.setTimeColumn("D_DATETIME");
        se.setFtpIp("10.40.17.38");
        se.setFtpPort(22);
        se.setFtpUser("sod");
        se.setFtpPwd("sod@2019");
        se.setSaveDirectory("/space/cmadaas/sod/sync_test/");
        se.setTransferType("local");
        se.setLastTime(from);
        ResultT r = new ResultT();
        syncUdToPcHandler.execute(se,r);
        r.setMsg(r.getProcessMsg().toString());


        return r;
    }
}

