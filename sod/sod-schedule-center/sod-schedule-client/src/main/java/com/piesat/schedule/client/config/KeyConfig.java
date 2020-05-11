package com.piesat.schedule.client.config;

import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.util.RedisUtil;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 18:03
 **/
@Component
public class KeyConfig implements ApplicationRunner {
    @Autowired
    private RedisUtil redisUtil;
    @Value("${gbaseserver.ip}")
    private String serverIp;
    @Value("${grpc.server.port}")
    private String serverPort;
    @Value("${backup.temp-path}")
    private String backupTempPath;
    protected static final String QUARTZ_HTHT_PERFORM="QUARTZ:HTHT:PERFORM";
    protected static final String QUARTZ_HTHT_TASK_SERIAL="QUARTZ:HTHT:SINGLE:SERIAL";
    protected static final String QUARTZ_HTHT_CLUSTER_SERIAL="QUARTZ:HTHT:CLUSTER:SERIAL";
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        FileUtil.delFile(new File(backupTempPath),new ResultT<String>());
        List<String> keys=redisUtil.scan(QUARTZ_HTHT_PERFORM+":"+serverIp+":"+serverPort);
        keys.forEach(key->
        {
            String v= (String) redisUtil.get(key);
            redisUtil.del(QUARTZ_HTHT_TASK_SERIAL+":"+v);
            redisUtil.del(QUARTZ_HTHT_CLUSTER_SERIAL+":"+v);
            redisUtil.del(key);
        });

    }
}

