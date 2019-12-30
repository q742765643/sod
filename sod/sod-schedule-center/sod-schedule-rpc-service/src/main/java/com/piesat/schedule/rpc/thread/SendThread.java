package com.piesat.schedule.rpc.thread;

import com.netflix.discovery.DiscoveryClient;
import com.netflix.discovery.shared.Application;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.rpc.enums.ExecuteEnum;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.sso.client.util.RedisUtil;
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
    //@Autowired
    private DiscoveryClient discoveryClient;
    public void init(){

      new Thread(()->{
          this.send();
      }).start();
    }
    public void send(){
        while (!sendThreadToStop){
            Object objects = redisUtil.reverseRange(QUARTZ_HTHT_WAIT, i, j);
            if(null!=objects){
                String key= (String) objects;
                JobInfoEntity jobInfo= (JobInfoEntity) redisUtil.get(QUARTZ_HTHT_JOBDTEAIL+key);
                if(jobInfo==null){
                    i=0;
                    j=0;
                    continue;
                }
                //this.execute(jobInfo);
                i++;
                j++;
            }else {
                i=0;
                j=0;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void execute(JobInfoEntity jobInfo){
        DiscoveryClient discoveryClient=SpringUtil.getBean(DiscoveryClient.class);
        Application a=discoveryClient.getApplication("dm-server");
        String serviceName= ExecuteEnum.getService(jobInfo.getType());
        try {
            ExecuteService executeService= (ExecuteService) SpringUtil.getBean(serviceName);
            executeService.insertLog(jobInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

