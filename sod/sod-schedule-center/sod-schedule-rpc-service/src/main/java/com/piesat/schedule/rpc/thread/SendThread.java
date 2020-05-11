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


    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisLock redisLock;
    public void init(){

      new Thread(()->{
          this.start();
      }).start();
    }
    public void start(){
        while (!sendThreadToStop) {
            try {
                this.send();
                Thread.sleep(1000);
            } catch (Exception e) {
                log.error("线程消费第一个WHILE:{}", OwnException.get(e));
            }
        }
    }
    public void send() {
            boolean flag=redisLock.tryLock("custom");
            if(flag){
                return;
            }
            try {
                int count=0;
                int i=0;
                int j=0;

                long startTime=System.currentTimeMillis();
                while (System.currentTimeMillis()-startTime<5000) {
                    try {
                        Set<Object> objects = redisUtil.reverseRange(QUARTZ_HTHT_WAIT, i, j);
                        if(objects.isEmpty()){//||count>1000
                            break;
                        }
                        if (!objects.isEmpty()) {
                            for (Object object : objects) {

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
                                    redisUtil.zsetRemove(QUARTZ_HTHT_WAIT, objects);
                                }
                                if(!resultT.isSuccess()){
                                    i++;
                                    j++;
                                    //Thread.sleep(6000);
                                }

                            }
                        }
                    } catch (Exception e) {
                        log.error("线程消费第二个WHILE异常:{}", OwnException.get(e));
                        break;
                    }

                }


            } finally {
                redisLock.delete("custom");

            }




    }

    public ResultT<String> execute(JobInfoEntity jobInfo){
        ResultT<String> resultT=new ResultT<>();
        try {
            String serviceName= ExecuteEnum.getService(jobInfo.getType());
            ExecuteService executeService= (ExecuteService) SpringUtil.getBean(serviceName);
            executeService.executeBusiness(jobInfo,resultT);
        } catch (Exception e) {
            resultT.setCode(301);
            e.printStackTrace();
        }
        return resultT;
    }
}

