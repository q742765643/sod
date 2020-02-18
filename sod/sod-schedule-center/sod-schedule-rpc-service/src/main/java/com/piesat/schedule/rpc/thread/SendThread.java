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
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-26 17:57
 **/
@Service
public class SendThread {
    private volatile boolean sendThreadToStop = false;
    private volatile int i = 0;
    private volatile int j = 0;
    private static final String QUARTZ_HTHT_WAIT="QUARTZ:HTHT:WAIT";
    private static final String QUARTZ_HTHT_JOBDTEAIL="QUARTZ:HTHT:JOBDTEAIL:";


    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisLock redisLock;
    public void init(){

      new Thread(()->{
          this.send();
      }).start();
    }
    public void send(){
        while (!sendThreadToStop){
            Object objects = null;
            try {
                redisLock.lock("custom");
                objects = redisUtil.reverseRange(QUARTZ_HTHT_WAIT, i, j);
                if(null==objects){
                    redisLock.delete("custom");
                    i=0;
                    j=0;
                    try {
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                if(null!=objects){
                    String key= (String) objects;
                    JobInfoEntity jobInfo= (JobInfoEntity) redisUtil.get(QUARTZ_HTHT_JOBDTEAIL+key);
                    if(jobInfo==null){
                        redisUtil.zsetRemove(QUARTZ_HTHT_WAIT,objects);
                        continue;
                    }
                    ResultT<String> resultT=this.execute(jobInfo);
                    if(!resultT.isSuccess()){
                        i++;
                        j++;
                    }
                    if(resultT.isSuccess()){
                        redisUtil.del(QUARTZ_HTHT_JOBDTEAIL+key);
                        redisUtil.zsetRemove(QUARTZ_HTHT_WAIT,objects);
                    }

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

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

