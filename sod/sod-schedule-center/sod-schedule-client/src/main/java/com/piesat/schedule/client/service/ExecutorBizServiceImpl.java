package com.piesat.schedule.client.service;

import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.client.business.BaseBusiness;
import com.piesat.schedule.client.enums.BusinessEnum;
import com.piesat.schedule.client.handler.backup.MetaBackupHandler;
import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.client.service.recover.DataBaseRecoverService;
import com.piesat.schedule.client.util.FileUtil;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.sso.client.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.text.Collator;
import java.util.*;

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
    @Autowired
    private DataBaseRecoverService dataBaseRecoverService;
    @Autowired
    private MetaBackupHandler metaBackupHandler;

    @Override
    public void execute(JobInfoEntity jobInfo){
        BaseHandler baseHandler= (BaseHandler) SpringUtil.getBean(jobInfo.getExecutorHandler());
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
    public void recover(MetaRecoverLogEntity recoverLogEntity){
        new Thread(()-> {
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
    public Map<String,Object> parsingPath(String path){
        return dataBaseRecoverService.parsingPath(path);
    }

    @Override
    public void handMetaBack(JobInfoEntity jobInfoEntity){
        new Thread(()->{
            metaBackupHandler.execute(jobInfoEntity);
        });
    }

    @Override
    public Object downFile(String path){
        byte[] bytes=FileUtil.File2byte(new File(path));
        System.out.println(bytes.length);
        return bytes;
    }
}
