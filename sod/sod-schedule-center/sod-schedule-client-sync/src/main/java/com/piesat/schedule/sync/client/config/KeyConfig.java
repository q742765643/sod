package com.piesat.schedule.sync.client.config;

import com.piesat.schedule.sync.client.util.FileUtil;
import com.piesat.util.ResultT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-27 18:03
 **/
@Component
public class KeyConfig implements ApplicationRunner {


    @Value("${sync.temp-path}")
    private String syncTempPath;

    @Override
    public void run(ApplicationArguments args) {
        FileUtil.delFile(new File(syncTempPath),new ResultT<String>());
    }
}

