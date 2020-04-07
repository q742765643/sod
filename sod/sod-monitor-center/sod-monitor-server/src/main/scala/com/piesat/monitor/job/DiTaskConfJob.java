package com.piesat.monitor.job;

import com.piesat.monitor.rpc.service.quartz.DiTaskConfService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-04-06 18:17
 **/
@Service
public class DiTaskConfJob implements Job {
    @Autowired
    private DiTaskConfService diTaskConfService;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        diTaskConfService.execute(jobExecutionContext);
    }
}

