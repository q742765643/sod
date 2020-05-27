package com.piesat.schedule.rpc.thread;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.Application;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.rpc.enums.ExecuteEnum;
import com.piesat.schedule.rpc.lock.RedisLock;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.sso.client.util.RedisUtil;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-26 17:57
 **/
@Slf4j
@Service
public class SendThread {
    private volatile boolean sendThreadToStop = false;

    private static final String QUARTZ_HTHT_WAIT="QUARTZ:HTHT:WAIT";
    private static final String QUARTZ_HTHT_JOBDTEAIL="QUARTZ:HTHT:JOBDTEAIL:";
    private static final String QUARTZ_HTHT_BLOCK="QUARTZ:HTHT:BLOCK";


    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisLock redisLock;
    public void init(){

        Thread sendThread = new Thread(this::start);
        sendThread.setDaemon(true);
        sendThread.setName("job -send");
        sendThread.start();
        Thread blockThread = new Thread(this::startBlock);
        blockThread.setDaemon(true);
        blockThread.setName("job -block");
        blockThread.start();
    }
    public void start(){
        while (!sendThreadToStop) {
            try {
                long start = System.currentTimeMillis();
                this.send();
                long cost = System.currentTimeMillis() - start;
                if (cost < 1000) {  // scan-overtime, not wait
                    try {
                        /**
                         *  pre-read period: success > scan each second; fail > skip this period;
                         *  */
                        TimeUnit.MILLISECONDS.sleep(1000- System.currentTimeMillis() % 1000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                        //Thread.currentThread().interrupt();
                    }
                }
            } catch (Exception e) {
                log.error("线程消费第一个WHILE:{}", OwnException.get(e));
            }
        }
    }
    public void startBlock(){
        while (!sendThreadToStop) {
            try {
                long start = System.currentTimeMillis();
                this.sendBlock();
                long cost = System.currentTimeMillis() - start;
                if (cost < 1000) {  // scan-overtime, not wait
                    try {
                        /**
                         *  pre-read period: success > scan each second; fail > skip this period;
                         *  */
                        TimeUnit.MILLISECONDS.sleep(1000- System.currentTimeMillis() % 1000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                        //Thread.currentThread().interrupt();
                    }
                }
            } catch (Exception e) {
                log.error("线程消费第一个WHILE:{}", OwnException.get(e));
            }
        }
    }
    public void send() {
            boolean flag=redisLock.trySendLock("custom");
            if(!flag){
                return;
            }
            try {
                int count=0;
                int i=0;
                int j=0;

                long startTime=System.currentTimeMillis();
                while (System.currentTimeMillis()-startTime<60*1000*3) {
                    try {
                        Set<Object> objects = redisUtil.reverseRange(QUARTZ_HTHT_WAIT, i, j);
                        if(objects.isEmpty()){//||count>1000
                            break;
                        }
                        if (!objects.isEmpty()) {
                            for (Object object : objects) {
                                long startTime1=System.currentTimeMillis();

                                String key = (String) object;
                                JobInfoEntity jobInfo = (JobInfoEntity) redisUtil.get(QUARTZ_HTHT_JOBDTEAIL + key);
                                if (jobInfo == null) {
                                    redisUtil.zsetRemove(QUARTZ_HTHT_WAIT, key);
                                    continue;
                                }


                                ResultT<String> resultT = this.execute(jobInfo);
                                if (resultT.isSuccess()) {
                                    count++;
                                    redisUtil.del(QUARTZ_HTHT_JOBDTEAIL + key);
                                    redisUtil.zsetRemove(QUARTZ_HTHT_WAIT, key);
                                }
                                if(!resultT.isSuccess()){
                                    if(resultT.getCode()!=309){
                                        redisUtil.zsetRemove(QUARTZ_HTHT_WAIT, key);
                                        long bcount = redisUtil.zsetCount(QUARTZ_HTHT_BLOCK);
                                        if (bcount < 20000) {
                                            redisUtil.zsetAdd(QUARTZ_HTHT_BLOCK, key, 2);
                                        }else {
                                            redisUtil.del(QUARTZ_HTHT_JOBDTEAIL + key);
                                        }

                                    }else{
                                        i++;
                                        j++;
                                    }
                                    //Thread.sleep(6000);
                                }
                                long endTime=System.currentTimeMillis();
                                log.info("调度耗时:{}ms", endTime-startTime1);

                            }
                        }
                    } catch (Exception e) {
                        i++;
                        j++;
                        log.error("线程消费第二个WHILE异常:{}", OwnException.get(e));
                        //break;
                    }

                }


            } finally {
                redisLock.delete("custom");

            }




    }
    public void sendBlock() {
        boolean flag=redisLock.trySendLock("block");
        if(!flag){
            return;
        }
        try {
            int count=0;
            int i=0;
            int j=0;

            long startTime=System.currentTimeMillis();
            while (System.currentTimeMillis()-startTime<60*1000*3) {
                try {
                    Set<Object> objects = redisUtil.reverseRange(QUARTZ_HTHT_BLOCK, i, j);
                    if(objects.isEmpty()){//||count>1000
                        break;
                    }
                    if (!objects.isEmpty()) {
                        for (Object object : objects) {

                            String key = (String) object;
                            JobInfoEntity jobInfo = (JobInfoEntity) redisUtil.get(QUARTZ_HTHT_JOBDTEAIL + key);
                            if (jobInfo == null) {
                                redisUtil.zsetRemove(QUARTZ_HTHT_BLOCK, key);
                                continue;
                            }


                            ResultT<String> resultT = this.execute(jobInfo);
                            if (resultT.isSuccess()) {
                                count++;
                                redisUtil.del(QUARTZ_HTHT_JOBDTEAIL + key);
                                redisUtil.zsetRemove(QUARTZ_HTHT_BLOCK, key);
                            }
                            if(!resultT.isSuccess()){
                                i++;
                                j++;

                            }

                        }
                    }
                } catch (Exception e) {
                    i++;
                    j++;
                    log.error("线程消费第二个WHILE异常:{}", OwnException.get(e));
                    //break;
                }

            }


        } finally {
            redisLock.delete("block");

        }




    }

    public ResultT<String> execute(JobInfoEntity jobInfo){
        ResultT<String> resultT=new ResultT<>();
        String serviceName="";
        try {
            serviceName= ExecuteEnum.getService(jobInfo.getType());
            ExecuteService executeService= (ExecuteService) SpringUtil.getBean(serviceName);
            executeService.executeBusiness(jobInfo,resultT);
        } catch (Exception e) {
            resultT.setCode(301);
            log.error("调度发送失败{},{}",OwnException.get(e),serviceName);
        }
        return resultT;
    }
}

