package com.piesat.schedule.rpc.config;

import com.piesat.schedule.rpc.thread.ScheduleThread;
import com.piesat.schedule.rpc.thread.SendThread;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-19 17:26
 **/
@Component
public class ScheduleConfig implements ApplicationRunner {
    @Autowired
    private ScheduleThread scheduleThread;
    @Autowired
    private SendThread sendThread;


    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {
        sendThread.init();
        scheduleThread.start();
    }
}

