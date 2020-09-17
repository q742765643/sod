package com.piesat.schedule.rpc.service;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.jpa.BaseDao;
import com.piesat.common.jpa.BaseService;
import com.piesat.common.utils.StringUtils;
import com.piesat.schedule.dao.JobInfoDao;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.mapper.JobInfoMapper;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.dto.JobInfoDto;
import com.piesat.schedule.rpc.enums.ExecuteEnum;
import com.piesat.schedule.rpc.lock.RedisLock;
import com.piesat.schedule.rpc.mapstruct.JobInfoMapstruct;
import com.piesat.schedule.rpc.service.execute.ExecuteService;
import com.piesat.schedule.rpc.service.statistics.TableCollectHandler;
import com.piesat.schedule.rpc.thread.ScheduleThread;
import com.piesat.schedule.util.CronExpression;
import com.piesat.sso.client.util.RedisUtil;
import com.piesat.ucenter.rpc.dto.dictionary.DefineDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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
    @Autowired
    private ScheduleThread scheduleThread;
    @Autowired
    private TableCollectHandler tableCollectHandler;





    @Override
    public BaseDao<JobInfoEntity> getBaseDao() {
        return jobInfoDao;
    }

    @Override
    public JobInfoDto saveDto(JobInfoDto jobInfoDto) {
        JobInfoEntity jobInfoEntity = this.jobInfoMapstruct.toEntity(jobInfoDto);
        jobInfoEntity = this.saveNotNull(jobInfoEntity);
        return this.jobInfoMapstruct.toDto(jobInfoEntity);
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
    public void init(){
        tableCollectHandler.init();
        List<JobInfoDto> jobInfoDtos=this.findJobList();
        for(JobInfoDto jobInfoDto:jobInfoDtos){
            Map<String,Object> map=new HashMap<>();
            map.put("type",jobInfoDto.getType());
            map.put("cron",jobInfoDto.getJobCron());
            redisUtil.hmset(QUARTZ_HTHT_CRON+jobInfoDto.getId(),map,-1);
            double score=0;
            if(!redisUtil.hasKey(QUARTZ_HTHT_JOB)){
                score=0;
            }else{
                score=redisUtil.zScore(QUARTZ_HTHT_JOB,jobInfoDto.getId());
            }
            try {
                Date nextValidTime = new CronExpression(jobInfoDto.getJobCron()).getNextValidTimeAfter(new Date());
                double x=score- nextValidTime.getTime();
                if(x>3*28800000||score==0){
                    redisUtil.zsetAdd(QUARTZ_HTHT_JOB,jobInfoDto.getId(),nextValidTime.getTime());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    public void start(JobInfoDto jobInfoDto){
        if(jobInfoDto.getTriggerStatus()==1){
            Map<String,Object> map=new HashMap<>();
            if(StringUtils.isNotNullString(jobInfoDto.getType())){
                map.put("type",jobInfoDto.getType());
            }else{
                String type=jobInfoMapper.findTypeById(jobInfoDto.getId());
                map.put("type",type);
            }
            map.put("cron",jobInfoDto.getJobCron());
            redisUtil.hmset(QUARTZ_HTHT_CRON+jobInfoDto.getId(),map,-1);

            double score=0;
            if(!redisUtil.hasKey(QUARTZ_HTHT_JOB)){
                score=0;
            }else{
                score=redisUtil.zScore(QUARTZ_HTHT_JOB,jobInfoDto.getId());
            }
            try {
                Date nextValidTime = new CronExpression(jobInfoDto.getJobCron()).getNextValidTimeAfter(new Date());
                double x=score- nextValidTime.getTime();
                if(x>3*28800000||score==0){
                    redisUtil.zsetAdd(QUARTZ_HTHT_JOB,jobInfoDto.getId(),nextValidTime.getTime());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
           /* try {
                Date nextValidTime = new CronExpression(jobInfoDto.getJobCron()).getNextValidTimeAfter(new Date());
                redisUtil.zsetAdd(QUARTZ_HTHT_JOB,jobInfoDto.getId(),nextValidTime.getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }*/

        }else{
            this.stop(jobInfoDto.getId());
        }

    }
    @Override
    public void stop(String id){
        if(redisUtil.hasKey(QUARTZ_HTHT_CRON+id)){
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

    public void execute(String id){
        String type=jobInfoMapper.findTypeById(id);
        String serviceName= ExecuteEnum.getService(type);
        ExecuteService executeService= (ExecuteService) SpringUtil.getBean(serviceName);
        JobInfoEntity jobInfoEntity=executeService.getById(id);
        jobInfoEntity.setTriggerLastTime(System.currentTimeMillis());
        jobInfoEntity.setType(type);
        scheduleThread.handT(jobInfoEntity);

    }
    public void executeB(String id,String time){
        String type=jobInfoMapper.findTypeById(id);
        String serviceName= ExecuteEnum.getService(type);
        ExecuteService executeService= (ExecuteService) SpringUtil.getBean(serviceName);
        JobInfoEntity jobInfoEntity=executeService.getById(id);
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date=simpleDateFormat.parse(time);

            Date nextValidTime = new CronExpression(jobInfoEntity.getJobCron()).getNextValidTimeAfter(date);
            if(nextValidTime.getTime()<System.currentTimeMillis()){
                jobInfoEntity.setType(type);
                jobInfoEntity.setTriggerLastTime(nextValidTime.getTime());
                scheduleThread.handT(jobInfoEntity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void startById(String id){
        jobInfoMapper.updateTriggerStatus(1,id);
        JobInfoEntity jobInfoEntity=jobInfoMapper.findById(id);
        this.start(jobInfoMapstruct.toDto(jobInfoEntity));
    }
    public void stopById(String id){
        jobInfoMapper.updateTriggerStatus(0,id);
        this.stop(id);
    }

}

