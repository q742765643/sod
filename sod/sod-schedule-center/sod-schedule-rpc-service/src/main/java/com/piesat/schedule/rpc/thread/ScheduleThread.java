package com.piesat.schedule.rpc.thread;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.enums.ExecuteEnum;
import com.piesat.schedule.rpc.lock.RedisLock;
import com.piesat.schedule.rpc.service.JobInfoLogService;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.schedule.util.CronExpression;
import com.piesat.sso.client.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.*;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-18 17:48
 **/
@Slf4j
@Service
public class  ScheduleThread {
    public static final long PRE_READ_MS = 5000;
    private static final String HTHT_LOCK = "htht_lock";
    private static final String QUARTZ_HTHT_JOB = "QUARTZ:HTHT:JOB";
    private static final String QUARTZ_HTHT_CRON = "QUARTZ:HTHT:CRON:";
    private static final String QUARTZ_HTHT_WAIT = "QUARTZ:HTHT:WAIT";
    private static final String QUARTZ_HTHT_JOBDTEAIL = "QUARTZ:HTHT:JOBDTEAIL:";
    public static final ExecutorService threadPool = new ThreadPoolExecutor(30, 30,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(20000), new ThreadFactoryBuilder().setNameFormat("do-schedule-pool-%d").build(), new ThreadPoolExecutor.AbortPolicy());
    private volatile  Map<Integer, List<JobInfoEntity>> ringData = new ConcurrentHashMap<>();
    private volatile boolean scheduleThreadToStop = false;
    private volatile boolean ringThreadToStop = false;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisLock redisLock;
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private JobInfoLogService jobInfoLogService;
    @Autowired
    private SpringClientFactory factory;

    public void start() {
        new Thread(()->{
            jobInfoService.init();
        }).start();

        Thread scheduleThread = new Thread(this::jobToTrriger);
        scheduleThread.setDaemon(true);
        scheduleThread.setName("job -run");
        scheduleThread.start();
        Thread reserveThread = new Thread(this::checkRight);
        reserveThread.setDaemon(true);
        reserveThread.setName("job -pre");
        reserveThread.start();
    }

    public  void jobToTrriger() {
        try {
            TimeUnit.MILLISECONDS.sleep(5000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
            //Thread.currentThread().interrupt();
        }
        /********=======1.准备调度限制条数========*******/
        while (!scheduleThreadToStop) {
            try {
                this.jobToTrrigerWhile();
            } catch (Exception e) {
                log.error("调度第一个WHILE:{}", OwnException.get(e));
            }
        }
    }
    public void jobToTrrigerWhile(){
            boolean flag=redisLock.tryLock(HTHT_LOCK);

            int preReadCount = 300;
            long start = System.currentTimeMillis();
            boolean preReadSuc = true;
            if(flag){
                try {
                    /********=======2.reis分布式锁========*******/

                    long nowTime = System.currentTimeMillis();
                    TimeZone timeZone=null;
                    try {
                         timeZone=TimeZone.getDefault();
                         if(timeZone.getID().toUpperCase().indexOf("GMT")!=-1){
                             nowTime=nowTime+3600000*8;
                         }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Set<DefaultTypedTuple> scheduleList = redisUtil.rangeByScoreWithScores(QUARTZ_HTHT_JOB, nowTime + PRE_READ_MS, preReadCount);
                    if (scheduleList != null && !scheduleList.isEmpty() ) {
                        this.checkSchedule(scheduleList, nowTime);
                    } else {
                        preReadSuc = false;
                    }

                } catch (Exception e) {
                    log.error(OwnException.get(e));
                } finally {
                    redisLock.delete(HTHT_LOCK);
                }
            }

            long cost = System.currentTimeMillis() - start;
            if (cost < 1000) {  // scan-overtime, not wait
                try {
                    /**
                     *  pre-read period: success > scan each second; fail > skip this period;
                     *  */
                    TimeUnit.MILLISECONDS.sleep((preReadSuc ? 1000 : PRE_READ_MS) - System.currentTimeMillis() % 1000);
                } catch (InterruptedException e) {
                    log.error(e.getMessage(), e);
                   //Thread.currentThread().interrupt();
                }
            }

    }
    public void checkSchedule(Set<DefaultTypedTuple> scheduleList, long nowTime) throws ParseException {
        List<JobInfoEntity> jobInfos = new ArrayList<>();
        for (DefaultTypedTuple typedTuple : scheduleList) {
            try {
                JobInfoEntity jobInfo = new JobInfoEntity();
                jobInfo.setId((String) typedTuple.getValue());
                jobInfo.setTriggerNextTime(typedTuple.getScore().longValue());
                String cron = (String) redisUtil.hget(QUARTZ_HTHT_CRON + jobInfo.getId(), "cron");
                if (cron == null) {
                    redisUtil.zsetRemove(QUARTZ_HTHT_JOB, jobInfo.getId());
                    continue;
                }
                jobInfo.setJobCron(cron);
                if (nowTime > jobInfo.getTriggerNextTime() + 3600000*12) {//PRE_READ_MS
                    log.info(">>>>>>>>>>> job, schedule misfire, jobId = {}" , typedTuple.getValue());
                    refreshNextValidTime(jobInfo, new Date());
                } else if (nowTime >=jobInfo.getTriggerNextTime()) {
                    log.info(">>>>>>>>>>> job, schedule push, jobId {}" ,typedTuple.getValue());
                    this.trigger(jobInfo);
                    refreshNextValidTime(jobInfo, new Date());
                    if (nowTime + PRE_READ_MS > jobInfo.getTriggerNextTime()) {
                        int ringSecond = (int) ((jobInfo.getTriggerNextTime() / 1000) % 60);

                        pushTimeRing(ringSecond, jobInfo);

                        refreshNextValidTime(jobInfo, new Date(jobInfo.getTriggerNextTime()));
                    }
                } else if(nowTime + PRE_READ_MS > jobInfo.getTriggerNextTime()){
                    int ringSecond = (int) ((jobInfo.getTriggerNextTime() / 1000) % 60);

                    pushTimeRing(ringSecond, jobInfo);

                    refreshNextValidTime(jobInfo, new Date(jobInfo.getTriggerNextTime()));


                }
                jobInfos.add(jobInfo);
            } catch (Exception e) {
                log.error("调度发生异常{}",OwnException.get(e));
            }

        }
        for (JobInfoEntity jobInfo : jobInfos) {
            redisUtil.zsetAdd(QUARTZ_HTHT_JOB, jobInfo.getId(), jobInfo.getTriggerNextTime());
        }
    }

    public void checkRight() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
           //Thread.currentThread().interrupt();
        }

        while (!ringThreadToStop) {
            try {
                this.checkRightwhile();
            } catch (Exception e) {
                log.error("调度第二个WHILE:{}", OwnException.get(e));
            }
        }
    }
    public void checkRightwhile(){
        try {
            // second data
            List<JobInfoEntity> ringItemData = new ArrayList<>();
            int nowSecond = Calendar.getInstance().get(Calendar.SECOND);   // 避免处理耗时太长，跨过刻度，向前校验一个刻度；
            for (int i = 0; i < 2; i++) {
                List<JobInfoEntity> tmpData = ringData.remove((nowSecond + 60 - i) % 60);
                if (tmpData != null) {
                    ringItemData.addAll(tmpData);
                }
            }

            if (!ringItemData.isEmpty()) {
                for (JobInfoEntity jobInfo : ringItemData) {
                    this.trigger(jobInfo);
                }
                ringItemData.clear();
            }
        } catch (Exception e) {
            log.error(OwnException.get(e));
        }
        try {
            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);

           //Thread.currentThread().interrupt();
        }
    }
    public void trigger(JobInfoEntity jobInfo) {
        threadPool.execute(() -> {
            try {
               /* long count = redisUtil.zsetCount(QUARTZ_HTHT_WAIT);
                if (count > 20000) {
                    log.info("积压条数超过2万,放弃调度");
                    return;
                }*/
                long count = redisUtil.scanSize(QUARTZ_HTHT_JOBDTEAIL);
                if (count > 20000) {
                    log.info("积压条数超过2万,放弃调度");
                    return;
                }
                String  type = (String) redisUtil.hget(QUARTZ_HTHT_CRON + jobInfo.getId(), "type");
                String serviceName = ExecuteEnum.getService(type);
                ExecuteService executeService = (ExecuteService) SpringUtil.getBean(serviceName);
                JobInfoEntity newJob = null;
                try {
                    newJob = executeService.getById(jobInfo.getId());
                } catch (Exception e) {
                    jobInfoService.stop(jobInfo.getId());
                    log.error(OwnException.get(e));
                    return;
                }
                if (1 != newJob.getTriggerStatus()) {
                    jobInfoService.stop(newJob.getId());
                    return;
                }
                newJob.setType(type);
                long triggerLastTime=jobInfo.getTriggerLastTime();
                if(triggerLastTime==0){
                    triggerLastTime=jobInfo.getTriggerNextTime();
                }
                newJob.setTriggerLastTime(triggerLastTime);
                newJob.setTriggerNextTime(jobInfo.getTriggerNextTime());
                this.pushRedis(newJob);
                log.info("执行成功:{}" ,jobInfo.getId());
            } catch (Exception e) {
                log.error(OwnException.get(e));
            }
        });


    }

    public void handT(JobInfoEntity jobInfo){
        threadPool.execute(() -> {
            try {
                String serviceName = ExecuteEnum.getService(jobInfo.getType());
                ExecuteService executeService = (ExecuteService) SpringUtil.getBean(serviceName);
                JobInfoEntity newJob = executeService.getById(jobInfo.getId());
                newJob.setType(jobInfo.getType());
                newJob.setTriggerLastTime(jobInfo.getTriggerLastTime());
                newJob.setTriggerNextTime(jobInfo.getTriggerNextTime());
                this.pushRedis(newJob);
                log.info("执行成功:{}" ,jobInfo.getId());
            } catch (Exception e) {
                log.error(OwnException.get(e));
            }
        });
    }

    public void pushRedis(JobInfoEntity newJob) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String date = format.format(new Date(System.currentTimeMillis()));
        String key = String.valueOf(99999999999999999L - Long.parseLong(date)) + newJob.getId();
        redisUtil.set(QUARTZ_HTHT_JOBDTEAIL + key, newJob, 86400);
        redisUtil.zsetAdd(QUARTZ_HTHT_WAIT, key, 2);

    }

    private void pushTimeRing(int ringSecond, JobInfoEntity jobInfo) {
        // push async ring
        List<JobInfoEntity> ringItemData = ringData.get(ringSecond);
        if (ringItemData == null) {
            ringItemData = new ArrayList<>();
            ringData.put(ringSecond, ringItemData);
        }
        ringItemData.add(jobInfo);

        log.debug(">>>>>>>>>>> xxl-job, schedule push time-ring : {} = {}" ,ringSecond,Arrays.asList(ringItemData));
    }

    private void refreshNextValidTime(JobInfoEntity jobInfo, Date fromTime) throws ParseException {
        Date nextValidTime = new CronExpression(jobInfo.getJobCron()).getNextValidTimeAfter(fromTime);
        if (nextValidTime != null) {
            jobInfo.setTriggerLastTime(jobInfo.getTriggerNextTime());
            jobInfo.setTriggerNextTime(nextValidTime.getTime());
        } else {
            jobInfo.setTriggerStatus(0);
            jobInfo.setTriggerLastTime(0L);
            jobInfo.setTriggerNextTime(0L);
        }
    }
}

