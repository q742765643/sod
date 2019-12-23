/*
package com.piesat.schedule.rpc.quartz;

import com.piesat.schedule.rpc.job.HthtJob;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

*/
/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-19 11:49
 **//*

@Service
public class QuartzService {
    @Autowired
    private Scheduler scheduler;

    */
/**
     * 新增一个定时任务
     * @param jName 任务名称
     * @param jGroup 任务组
     * @param tName 触发器名称
     * @param tGroup 触发器组
     * @param cron cron表达式
     *//*

    public void addJob(String jName, String jGroup, String tName, String tGroup, String cron) {
        try {
            JobDetail jobDetail = JobBuilder.newJob(HthtJob.class)
                    .withIdentity(jName, jGroup)
                    .build();
            CronTrigger trigger = TriggerBuilder.newTrigger()
                    .withIdentity(tName, tGroup)
                    .startNow()
                    .withSchedule(CronScheduleBuilder.cronSchedule(cron))
                    .build();
            scheduler.start();
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    */
/**
     * 暂停定时任务
     * @param jName 任务名
     * @param jGroup 任务组
     *//*

    public void pauseJob(String jName, String jGroup) {
        try {
            scheduler.pauseJob(JobKey.jobKey(jName, jGroup));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    */
/**
     * 继续定时任务
     * @param jName 任务名
     * @param jGroup 任务组
     *//*

    public void resumeJob(String jName, String jGroup) {
        try {
            scheduler.resumeJob(JobKey.jobKey(jName, jGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

    */
/**
     * 删除定时任务
     * @param jName 任务名
     * @param jGroup 任务组
     *//*

    public void deleteJob(String jName, String jGroup) {
        try {
            scheduler.deleteJob(JobKey.jobKey(jName, jGroup));
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}

*/
