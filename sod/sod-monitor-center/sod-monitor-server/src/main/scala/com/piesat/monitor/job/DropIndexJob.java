package com.piesat.monitor.job;

import com.piesat.monitor.rpc.service.quartz.DropIndexService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-04-07 10:33
 **/
public class DropIndexJob implements Job {
    @Autowired
    private DropIndexService dropIndexService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        dropIndexService.execute(jobExecutionContext);
    }
}

