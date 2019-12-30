package com.piesat.schedule.client.service;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.entity.JobInfoEntity;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:22
 */
@Service
public class ExecutorBizServiceImpl implements ExecutorBiz {
    @Override
    public void execute(JobInfoEntity jobInfo){
        BaseHandler baseHandler= (BaseHandler) SpringUtil.getBean(jobInfo.getType().toLowerCase()+"Handler");
        new Thread(()->{
            baseHandler.execute(jobInfo);
        }).start();
    }
}
