package com.piesat.schedule.rpc.thread;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.Application;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.rpc.enums.ExecuteEnum;
import com.piesat.schedule.rpc.lock.RedisLock;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.sso.client.util.RedisUtil;
import com.piesat.util.ResultT;
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
            this.send();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public void send() {
            try {
                long startTime=System.currentTimeMillis();
                int count=0;
                int i=0;
                int j=0;
                boolean flag=redisLock.tryLock("custom");
                while (flag) {
                    long mis=System.currentTimeMillis()-startTime;
                    Set<Object> objects = redisUtil.reverseRange(QUARTZ_HTHT_WAIT, i, j);
                    if(objects==null||objects.isEmpty()||count>100||mis>5000){
                        break;
                    }
                    if (null != objects && !objects.isEmpty()) {
                        for (Object object : objects) {

                            String key = (String) object;
                            JobInfoEntity jobInfo = (JobInfoEntity) redisUtil.get(QUARTZ_HTHT_JOBDTEAIL + key);
                            if (jobInfo == null) {
                                redisUtil.zsetRemove(QUARTZ_HTHT_WAIT, objects);
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
                            }

                        }
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

