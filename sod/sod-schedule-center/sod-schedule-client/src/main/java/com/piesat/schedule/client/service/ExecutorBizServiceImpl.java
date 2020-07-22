package com.piesat.schedule.client.service;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.handler.backup.MetaBackupHandler;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.recover.DataBaseRecoverService;
import com.piesat.schedule.client.service.recover.DataRecoverService;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.client.util.RedisUtil;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;
import java.io.File;
import java.text.Collator;
import java.util.*;
import java.util.concurrent.*;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:22
 */
@Slf4j
@Service
public class ExecutorBizServiceImpl implements ExecutorBiz {
    protected static final String QUARTZ_HTHT_PERFORM="QUARTZ:HTHT:PERFORM";
    protected static final String QUARTZ_HTHT_TASK_SERIAL="QUARTZ:HTHT:SINGLE:SERIAL";
    protected static final String QUARTZ_HTHT_CLUSTER_SERIAL="QUARTZ:HTHT:CLUSTER:SERIAL";

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private DataBaseRecoverService dataBaseRecoverService;
    @Autowired
    private MetaBackupHandler metaBackupHandler;
    @Autowired
    private DataRecoverService dataRecoverService;
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
    @Override
    public List<TreeVo> findMeta(String parentId, String databaseType){
        BusinessEnum businessEnum = BusinessEnum.match(databaseType, null);
        BaseBusiness baseBusiness = businessEnum.getBaseBusiness();
        List<TreeVo> treeVos=baseBusiness.findMeta(parentId);
        Collections.sort(treeVos, new Comparator<TreeVo>() {
            @Override
            public int compare(TreeVo o1, TreeVo o2) {
                //这里俩个是对属性判null处理，为null的都放到列表最下面
                if (null==o1.getName()){
                    return 1;
                }
                if (null==o2.getName()){
                    return -1;
                }
                return Collator.getInstance(Locale.CHINESE).compare(o1.getName(),o2.getName());
            }
        });
        return treeVos;
    }
    @Override
    public List<TreeVo> findAllTableByIp(String parentId, String databaseType){
        BusinessEnum businessEnum = BusinessEnum.match(databaseType, null);
        BaseBusiness baseBusiness = businessEnum.getBaseBusiness();
        List<TreeVo> treeVos=baseBusiness.findAllTableByIp(parentId);
        Collections.sort(treeVos, new Comparator<TreeVo>() {
            @Override
            public int compare(TreeVo o1, TreeVo o2) {
                //这里俩个是对属性判null处理，为null的都放到列表最下面
                if (null==o1.getName()){
                    return 1;
                }
                if (null==o2.getName()){
                    return -1;
                }
                return Collator.getInstance(Locale.CHINESE).compare(o1.getName(),o2.getName());
            }
        });
        return treeVos;
    }

    @Override
    public void recover(MetaRecoverLogEntity recoverLogEntity){
        executorService.execute(()->{
                dataBaseRecoverService.recover(recoverLogEntity);
            });

    }

    @Override
    public  List<TreeVo> getFileList(String parentId, String storageDirectory){
        return dataBaseRecoverService.getFileList(parentId,storageDirectory);
    }
    @Override
    public List<TreeVo> getFileChidren(String childrenPath){
        return dataBaseRecoverService.getFileChidren(childrenPath);
    }
    @Override
    public  List<TreeVo> getDataFileList(RecoverLogEntity recoverLogEntity){
        return dataBaseRecoverService.getDataFileList(recoverLogEntity);
    }
    @Override
    public Map<String,Object> parsingPath(String path){
        return dataBaseRecoverService.parsingPath(path);
    }

    @Override
    public void handMetaBack(JobInfoEntity jobInfoEntity){
        executorService.execute(()->{
            metaBackupHandler.execute(jobInfoEntity,new ResultT<String>());
        });
    }
    @Override
    public void recoverStructedData(RecoverLogEntity recoverLogEntity){
        executorService.execute(()->{
            dataRecoverService.recoverStructedData(recoverLogEntity);
        });
    }
    @Override
    public List<Map<String,Object>> md5Check(List<String> paths){
         return dataRecoverService.md5Check(paths);
    }



}
