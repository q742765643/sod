package com.piesat.schedule.rpc.thread;

import com.piesat.schedule.entity.JobInfoEntity;
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

    @Autowired
    private RedisUtil redisUtil;
    public void init(){

      new Thread(()->{


      });
    }
    public void send(){
        while (!sendThreadToStop){
            Object objects = redisUtil.reverseRange(QUARTZ_HTHT_WAIT, 0, 0);
            if(null!=objects){
                JobInfoEntity job= (JobInfoEntity) objects;
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
}

