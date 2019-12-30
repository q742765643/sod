package com.piesat.schedule.rpc.config;

import com.piesat.schedule.rpc.thread.ScheduleThread;
import com.piesat.schedule.rpc.thread.SendThread;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-19 17:26
 **/
@Component
public class ScheduleConfig implements InitializingBean, DisposableBean {
    @Autowired
    private ScheduleThread scheduleThread;
    @Override
    public void destroy() throws Exception {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduleThread.start();
    }
}

