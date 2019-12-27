package com.piesat.schedule.rpc.service;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.dm.rpc.api.DataLogicService;
import com.piesat.dm.rpc.api.DataTableService;
import com.piesat.dm.rpc.api.DatabaseService;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.schedule.dao.JobInfoDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.dto.JobInfoDto;
import com.piesat.schedule.rpc.lock.RedisLock;
import com.piesat.schedule.rpc.mapstruct.JobInfoMapstruct;
import com.piesat.schedule.util.CronExpression;
import com.piesat.sso.client.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-20 16:29
 **/
@Service
public class JobInfoServiceImpl extends BaseService<JobInfoEntity> implements JobInfoService{
    private static final String QUARTZ_HTHT_JOB="QUARTZ:HTHT:JOB";
    private static final String QUARTZ_HTHT_CRON="QUARTZ:HTHT:CRON:";

    @Autowired
    private JobInfoDao jobInfoDao;
    @Autowired
    private JobInfoMapstruct jobInfoMapstruct;
    @Autowired
    private JobInfoMapper jobInfoMapper;
    @Autowired
    private RedisUtil redisUtil;


    @GrpcHthtClient
    private DatabaseService databaseService;
    @GrpcHthtClient
    private DataTableService dataTableService;
    @GrpcHthtClient
    private DataLogicService dataLogicService;


    @Override
    public BaseDao<JobInfoEntity> getBaseDao() {
        return jobInfoDao;
    }
    @Override
    public List<JobInfoDto> findJobList(){
        List<JobInfoEntity> jobInfoEntities=jobInfoMapper.findJobList();
        return jobInfoMapstruct.toDto(jobInfoEntities);
    }
    @Override
    public Object getJobById(String id){
        return this.getById(id);
    }
    @Override
    public List<DatabaseDto> findAllDataBase(){
        List<DatabaseDto> databaseDtos=databaseService.all();
        return databaseDtos;
    }
    @Override
    public List<Map<String, Object>> getByDatabaseId(String databaseId){
        List<Map<String, Object>> mapList=dataLogicService.getByDatabaseId(databaseId);
        return mapList;
    }
    @Override
    public Map<String,String> getByDatabaseIdAndClassId(String databaseId,String dataClassId){
        List<DataTableDto>  dataTableDtos=dataTableService.getByDatabaseIdAndClassId(databaseId,dataClassId);
        Map<String,String> map=new HashMap<>();
        if(dataTableDtos.size()>1){
            for(DataTableDto dataTableDto:dataTableDtos){
               if("K".equals(dataTableDto.getDbTableType())){
                   map.put("tableName",dataTableDto.getTableName());
               }else{
                   map.put("vTableName",dataTableDto.getTableName());
               }
            }
        }
        if(dataTableDtos.size()==1)
        {
            map.put("tableName",dataTableDtos.get(0).getTableName());
            map.put("vTableName","");
        }
        return map;

    }
    @Override
    public void init(){
        List<JobInfoDto> jobInfoDtos=this.findJobList();
        for(JobInfoDto jobInfoDto:jobInfoDtos){
            redisUtil.set(QUARTZ_HTHT_CRON+jobInfoDto.getId(),jobInfoDto.getJobCron(),-1);
            double score=0;
            if(!redisUtil.hasKey(QUARTZ_HTHT_JOB)){
                score=0;
            }else{
                score=redisUtil.zScore(QUARTZ_HTHT_JOB,jobInfoDto.getId());
            }
            if(score<=0){
                try {
                    Date nextValidTime = new CronExpression(jobInfoDto.getJobCron()).getNextValidTimeAfter(new Date());
                    redisUtil.zsetAdd(QUARTZ_HTHT_JOB,jobInfoDto.getId(),nextValidTime.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @Override
    public void start(JobInfoDto jobInfoDto){
        if(jobInfoDto.getTriggerStatus()==1){
            redisUtil.set(QUARTZ_HTHT_CRON+jobInfoDto.getId(),jobInfoDto.getJobCron(),-1);
            double score=0;
            if(!redisUtil.hasKey(QUARTZ_HTHT_JOB)){
                score=0;
            }else{
                score=redisUtil.zScore(QUARTZ_HTHT_JOB,jobInfoDto.getId());
            }
            if(score<=0){
                try {
                    Date nextValidTime = new CronExpression(jobInfoDto.getJobCron()).getNextValidTimeAfter(new Date());
                    redisUtil.zsetAdd(QUARTZ_HTHT_JOB,jobInfoDto.getId(),nextValidTime.getTime());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        }else{
            this.stop(jobInfoDto.getId());
        }

    }
    @Override
    public void stop(String id){
        if(redisUtil.hasKey(id)){
            redisUtil.del(QUARTZ_HTHT_CRON+id);
        }
        redisUtil.zsetRemove(QUARTZ_HTHT_JOB,id);
    }
    @Override
    public void stopByIds(List<String> ids){
        for(String id:ids){
            this.stop(id);
        }
    }
}

