package com.piesat.schedule.client.controller;

import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-05-25 20:52
 **/
@RestController
public class TestController {
    @Value("${spring.datasource.password}")
    private String pwd;
    @Value("${spring.redis.password}")
    private String redis;
    @Value("${gbaseserver.pass}")
    private String gbase;
    @PostMapping("/test")
    public ResultT<Map<String,String>> test(){
        ResultT<Map<String,String>> resultT=new ResultT<>();
        Map<String,String> map=new HashMap<>();
        map.put("redis",redis);
        map.put("pwd",pwd);
        map.put("gbase",gbase);
        resultT.setData(map);
        return resultT;
    }
}

