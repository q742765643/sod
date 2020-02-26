package com.piesat.schedule.client.service;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.sso.client.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:22
 */
@Service
public class ExecutorBizServiceImpl implements ExecutorBiz {
    protected static final String QUARTZ_HTHT_PERFORM="QUARTZ:HTHT:PERFORM";
    protected static final String QUARTZ_HTHT_TASK_SERIAL="QUARTZ:HTHT:SINGLE:SERIAL";
    protected static final String QUARTZ_HTHT_CLUSTER_SERIAL="QUARTZ:HTHT:CLUSTER:SERIAL";

    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void execute(JobInfoEntity jobInfo){
        BaseHandler baseHandler= (BaseHandler) SpringUtil.getBean(jobInfo.getType().toLowerCase()+"Handler");
        new Thread(()->{
            try {
                baseHandler.execute(jobInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finally {
                redisUtil.del(QUARTZ_HTHT_PERFORM+":"+jobInfo.getExecutorAddress()+":"+jobInfo.getId());
                redisUtil.del(QUARTZ_HTHT_TASK_SERIAL+":"+jobInfo.getId());
                redisUtil.del(QUARTZ_HTHT_CLUSTER_SERIAL+":"+jobInfo.getId());
            }
        }).start();
    }
    public List<TreeVo> findMeta(String parentId,String databaseType){
        BusinessEnum businessEnum = BusinessEnum.match(databaseType, null);
        BaseBusiness baseBusiness = businessEnum.getBaseBusiness();

        return baseBusiness.findMeta(parentId);
    }
}
