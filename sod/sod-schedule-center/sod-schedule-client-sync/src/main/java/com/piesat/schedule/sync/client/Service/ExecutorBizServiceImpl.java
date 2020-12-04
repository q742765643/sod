package com.piesat.schedule.sync.client.Service;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.sync.client.api.ExecutorSync;
import com.piesat.schedule.sync.client.handler.base.BaseHandler;
import com.piesat.schedule.sync.client.util.RedisUtil;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:22
 */
@Slf4j
@Service
public class ExecutorBizServiceImpl implements ExecutorSync {
    protected static final String QUARTZ_HTHT_PERFORM="QUARTZ:HTHT:PERFORM";
    protected static final String QUARTZ_HTHT_TASK_SERIAL="QUARTZ:HTHT:SINGLE:SERIAL";
    protected static final String QUARTZ_HTHT_CLUSTER_SERIAL="QUARTZ:HTHT:CLUSTER:SERIAL";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ExecutorService executorService;

    @Override
    public void execute(JobInfoEntity jobInfo){
        executorService.execute(
                ()->{
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    FutureTask<String> future = new FutureTask<String>(new Callable<String>() {
                        @Override
                        public String call() throws Exception {
                            executeTimeOut(jobInfo);
                            return "00";
                        }
                    });
                    executor.execute(future);
                    try {
                        String result = future.get(1, TimeUnit.DAYS);
                        log.info("返回结果{}",result);
                    } catch (InterruptedException e) {
                        log.error("线程异常{}", OwnException.get(e));
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        // TODO Auto-generated catch block
                        log.error("线程异常{}", OwnException.get(e));
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        log.error("线程超时异常{}", OwnException.get(e));
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }finally{
                        log.info("关闭线程");
                        future.cancel(true);
                        executor.shutdown();
                    }
                });
    }
    public void executeTimeOut(JobInfoEntity jobInfo){
        try {
            int i=0;
            boolean flag=false;
            if(null==jobInfo.getExecutorFailRetryCount()){
                jobInfo.setExecutorFailRetryCount(0);
            }
            if(jobInfo.getExecutorFailRetryCount()==0){
                jobInfo.setExecutorFailRetryCount(1);
            }
            while (i<=jobInfo.getExecutorFailRetryCount()&&!flag){
                ResultT<String> resultT=new ResultT<>();
                this.executeJob(jobInfo,resultT);
                if(resultT.isSuccess()){
                    flag=true;
                }else {
                    Thread.sleep(500);
                }
                if(i>=1){
                    log.info("id:{},重试第{}次",jobInfo.getId(),i);
                }
                i++;


            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            boolean flag=false;
            while (!flag){
                try {
                    redisUtil.del(QUARTZ_HTHT_PERFORM+":"+jobInfo.getExecutorAddress()+":"+jobInfo.getId());
                    redisUtil.del(QUARTZ_HTHT_TASK_SERIAL+":"+jobInfo.getId());
                    redisUtil.del(QUARTZ_HTHT_CLUSTER_SERIAL+":"+jobInfo.getId());
                    flag=true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public void executeJob(JobInfoEntity jobInfo,ResultT<String> resultT){
        try {
            BaseHandler baseHandler= (BaseHandler) SpringUtil.getBean(jobInfo.getExecutorHandler());
            baseHandler.execute(jobInfo,resultT);
        } catch (Exception e) {
            resultT.setErrorMessage(OwnException.get(e));
            e.printStackTrace();
        }
    }

}
