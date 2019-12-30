package com.piesat.schedule.rpc.thread;

import com.alibaba.fastjson.JSON;
import com.netflix.loadbalancer.ILoadBalancer;
import com.netflix.loadbalancer.IRule;
import com.netflix.loadbalancer.Server;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.JobInfoLogEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.dto.JobInfoDto;
import com.piesat.schedule.rpc.enums.ExecuteEnum;
import com.piesat.schedule.rpc.lock.RedisLock;
import com.piesat.schedule.rpc.service.JobInfoLogService;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.schedule.util.CronExpression;
import com.piesat.sso.client.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.ribbon.SpringClientFactory;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.bouncycastle.asn1.x500.style.RFC4519Style.o;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-18 17:48
 **/
@Service
public class ScheduleThread {
    private Logger log= LoggerFactory.getLogger(ScheduleThread.class);
    private volatile boolean scheduleThreadToStop = false;
    private volatile boolean ringThreadToStop = false;
    public static final long PRE_READ_MS = 5000;
    private static final String HTHT_LOCK="htht_lock";
    private static final String QUARTZ_HTHT_JOB="QUARTZ:HTHT:JOB";
    private static final String QUARTZ_HTHT_CRON="QUARTZ:HTHT:CRON:";
    private static final String QUARTZ_HTHT_WAIT="QUARTZ:HTHT:WAIT";
    private static final String QUARTZ_HTHT_JOBDTEAIL="QUARTZ:HTHT:JOBDTEAIL:";
    private Thread scheduleThread;
    private Thread reserveThread;
    private volatile static Map<Integer, List<JobInfoEntity>> ringData = new ConcurrentHashMap<>();
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
    public void start(){
        jobInfoService.init();

        scheduleThread=new Thread(
                ()->{

                    this.getJobToTrriger();

                }
        );
        scheduleThread.setDaemon(true);
        scheduleThread.setName("job -run");
        scheduleThread.start();
        reserveThread=new Thread(
            ()->{
                this.checkRight();
            });

        reserveThread.setDaemon(true);
        reserveThread.setName("job -pre");
        reserveThread.start();
    }

    public void getJobToTrriger(){
        try {
            TimeUnit.MILLISECONDS.sleep(5000 - System.currentTimeMillis()%1000 );
        } catch (InterruptedException e) {
            if (!scheduleThreadToStop) {
                log.error(e.getMessage(), e);
            }
        }
        /********=======1.准备调度限制条数========*******/
        int preReadCount = 300;
        while (!scheduleThreadToStop) {
            long start = System.currentTimeMillis();
            boolean preReadSuc = true;
            try {
                /********=======2.reis分布式锁========*******/
                redisLock.lock(HTHT_LOCK);
                long nowTime = System.currentTimeMillis();

                Set<DefaultTypedTuple> scheduleList=redisUtil.rangeByScoreWithScores(QUARTZ_HTHT_JOB,nowTime+PRE_READ_MS,preReadCount);
                if (scheduleList!=null && scheduleList.size()>0) {
                    this.checkSchedule(scheduleList,nowTime);

                }else{
                    preReadSuc=false;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                redisLock.delete(HTHT_LOCK);
            }
            long cost = System.currentTimeMillis()-start;
            if (cost < 1000) {  // scan-overtime, not wait
                try {
                    // pre-read period: success > scan each second; fail > skip this period;
                    TimeUnit.MILLISECONDS.sleep((preReadSuc?1000:PRE_READ_MS) - System.currentTimeMillis()%1000);
                } catch (InterruptedException e) {
                    if (!scheduleThreadToStop) {
                        log.error(e.getMessage(), e);
                    }
                }
            }

        }
    }

    public void checkSchedule(Set<DefaultTypedTuple> scheduleList,long nowTime) throws ParseException {
        List<JobInfoEntity> jobInfos=new ArrayList<>();
        for (DefaultTypedTuple typedTuple: scheduleList) {
            JobInfoEntity jobInfo=new JobInfoEntity();
            jobInfo.setId((String) typedTuple.getValue());
            jobInfo.setTriggerNextTime(new Double(typedTuple.getScore()).longValue());
            String cron = (String) redisUtil.hget(QUARTZ_HTHT_CRON+jobInfo.getId(),"cron");
            if(cron==null){
                redisUtil.zsetRemove(QUARTZ_HTHT_JOB,jobInfo.getId());
                continue;
            }
            jobInfo.setJobCron(cron);
            if (nowTime > jobInfo.getTriggerNextTime() + PRE_READ_MS) {
                log.info(">>>>>>>>>>> job, schedule misfire, jobId = " + typedTuple.getValue());
                refreshNextValidTime(jobInfo, new Date());
            }else if (nowTime > jobInfo.getTriggerNextTime()){
                log.info(">>>>>>>>>>> job, schedule push, jobId = " + typedTuple.getValue());
                this.trigger(jobInfo);
                refreshNextValidTime(jobInfo, new Date());
                if(nowTime+ PRE_READ_MS > jobInfo.getTriggerNextTime()){
                    int ringSecond = (int)((jobInfo.getTriggerNextTime()/1000)%60);

                    pushTimeRing(ringSecond, jobInfo);

                    refreshNextValidTime(jobInfo, new Date(jobInfo.getTriggerNextTime()));
                }
            }else {
                int ringSecond = (int)((jobInfo.getTriggerNextTime()/1000)%60);

                pushTimeRing(ringSecond, jobInfo);

                refreshNextValidTime(jobInfo, new Date(jobInfo.getTriggerNextTime()));


            }
            jobInfos.add(jobInfo);

        }
        for (JobInfoEntity jobInfo: jobInfos) {
            redisUtil.zsetAdd(QUARTZ_HTHT_JOB,jobInfo.getId(),jobInfo.getTriggerNextTime());
        }
    }
    public void checkRight() {
        try {
            TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
        } catch (InterruptedException e) {
            if (!ringThreadToStop) {
                log.error(e.getMessage(), e);
            }
        }

        while (!ringThreadToStop) {

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

                if (ringItemData.size() > 0) {
                    for (JobInfoEntity jobInfo : ringItemData) {
                        this.trigger(jobInfo);
                    }
                    ringItemData.clear();
                }
            } catch (Exception e) {
                if (!ringThreadToStop) {
                }
            }
            try {
                TimeUnit.MILLISECONDS.sleep(1000 - System.currentTimeMillis() % 1000);
            } catch (InterruptedException e) {
                if (!ringThreadToStop) {
                    log.error(e.getMessage(), e);
                }
            }
        }
    }
    private void trigger(JobInfoEntity jobInfo){
        try {
            ILoadBalancer lb =  factory.getLoadBalancer("dm-server"); //指定服务名
            List<Server> allServers = lb.getAllServers();
            List<Server> upServers = lb.getReachableServers();
            String type= (String) redisUtil.hget(QUARTZ_HTHT_CRON+jobInfo.getId(),"type");
            String serviceName= ExecuteEnum.getService(type);
            ExecuteService executeService= (ExecuteService) SpringUtil.getBean(serviceName);

            JobInfoEntity newJob= executeService.getById(jobInfo.getId());
            if(1!=newJob.getTriggerStatus()){
                jobInfoService.stop(newJob.getId());
                return;
            }
            newJob.setType(type);
            if("1".equals(newJob.getIsRedo())){
                JobInfoLogEntity jobInfoLogEntity=jobInfoLogService.selectMaxTriggerTimeByJobId(newJob.getId());
                if(null!=jobInfoLogEntity){
                    newJob.setTriggerLastTime(jobInfoLogEntity.getTriggerTime());
                    if(1!=jobInfoLogEntity.getHandleCode()){
                        //redisUtil.zsetAdd(QUARTZ_HTHT_WAIT, newJob,-newJob.getTriggerLastTime());
                        this.pushRedis(newJob);
                    }
                    while (newJob.getTriggerLastTime()<jobInfo.getTriggerLastTime()){
                        //redisUtil.zsetAdd(QUARTZ_HTHT_WAIT, newJob,-newJob.getTriggerLastTime());
                        this.pushRedis(newJob);
                    }

                }

            }
            newJob.setTriggerLastTime(jobInfo.getTriggerLastTime());
            newJob.setTriggerNextTime(jobInfo.getTriggerNextTime());
            //redisUtil.zsetAdd(QUARTZ_HTHT_WAIT, newJob,-jobInfo.getTriggerLastTime());
            this.pushRedis(newJob);

        /*Object objects = redisUtil.reverseRange(QUARTZ_HTHT_WAIT, 0, 0);
        JobInfoEntity job= (JobInfoEntity) objects;*/
            log.info("执行成功:"+jobInfo.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void pushRedis(JobInfoEntity newJob){
        SimpleDateFormat format=new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String date=format.format(new Date(System.currentTimeMillis()));
        String key=String.valueOf(99999999999999999L-Long.parseLong(date))+newJob.getId();
        redisUtil.set(QUARTZ_HTHT_JOBDTEAIL+key,newJob,86400);
        redisUtil.zsetAdd(QUARTZ_HTHT_WAIT,key,2);

    }
    private void pushTimeRing(int ringSecond, JobInfoEntity jobInfo){
        // push async ring
        List<JobInfoEntity> ringItemData = ringData.get(ringSecond);
        if (ringItemData == null) {
            ringItemData = new ArrayList<JobInfoEntity>();
            ringData.put(ringSecond, ringItemData);
        }
        ringItemData.add(jobInfo);

        log.debug(">>>>>>>>>>> xxl-job, schedule push time-ring : " + ringSecond + " = " + Arrays.asList(ringItemData) );
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

