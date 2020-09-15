package com.piesat.schedule.rpc.service.nas.quota;

import com.alibaba.fastjson.JSON;
import com.piesat.util.ResultT;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName : HauweiQuota
 * @Description :
 * @Author : zzj
 * @Date: 2020-09-14 17:56
 */
public class HauweiQuota extends BaseQuota{
    @Override
    public ResultT<String> add(String path, BigDecimal hardThreshold) {
        ResultT<String> resultT=new ResultT<>();
        Map<String,Object> map=new HashMap<>();
        map.put("parentType",16400);
        map.put("parentID",path);
        map.put("resourceType",new Integer[]{1});
        map.put("hardLimit",hardThreshold.doubleValue());
        map.put("monitor",false);
        map.put("treeName","");
        map.put("userOrGrpName","everyone");
        map.put("userType",1);
        map.put("domainType",2);
        String url="https://10.41.15.83:8088/deviceManager/v1/rest/huaweicluster21544767616291/fsquota";
        resultT=this.send(url, JSON.toJSONString(map));
        return resultT;
    }

    @Override
    public ResultT<String> update(String path, BigDecimal hardThreshold) {
        return null;
    }

    @Override
    public ResultT<String> del(String path) {
        return null;
    }
}

