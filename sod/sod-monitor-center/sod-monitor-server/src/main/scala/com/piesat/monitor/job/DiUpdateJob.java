package com.piesat.monitor.job;

import com.piesat.monitor.rpc.service.quartz.DiUpdateService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-04-07 13:51
 **/
@Service
public class DiUpdateJob implements Job {
    @Autowired
    private DiUpdateService diUpdateService;
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        diUpdateService.execute(jobExecutionContext);
    }
}

