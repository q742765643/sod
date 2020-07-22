package com.piesat.schedule.web.controller.job;

import com.alibaba.fastjson.JSON;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.common.grpc.config.ChannelUtil;
import com.piesat.common.grpc.config.GrpcAutoConfiguration;
import com.piesat.common.grpc.config.SpringUtil;
import com.piesat.common.utils.OwnException;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.schedule.dao.backup.BackupDao;
import com.piesat.schedule.dao.clear.ClearDao;
import com.piesat.schedule.dao.move.MoveDao;
import com.piesat.schedule.entity.backup.BackupEntity;
import com.piesat.schedule.entity.clear.ClearEntity;
import com.piesat.schedule.entity.move.MoveEntity;
import com.piesat.schedule.rpc.api.JobInfoService;
import com.piesat.schedule.rpc.api.backup.BackupService;
import com.piesat.schedule.rpc.api.clear.ClearService;
import com.piesat.schedule.rpc.api.move.MoveService;
import com.piesat.schedule.rpc.dto.JobInfoDto;
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.clear.ClearDto;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.schedule.rpc.mapstruct.backup.BackupMapstruct;
import com.piesat.schedule.rpc.mapstruct.clear.ClearMapstruct;
import com.piesat.schedule.rpc.mapstruct.move.MoveMapstruct;
import com.piesat.schedule.rpc.proxy.GrpcServiceProxy;
import com.piesat.schedule.rpc.service.DataBaseService;
import com.piesat.schedule.rpc.service.DiSendService;
import com.piesat.schedule.rpc.vo.DataRetrieval;
import com.piesat.schedule.util.CronExpression;
import com.piesat.sso.client.util.RedisUtil;
import com.piesat.util.ResultT;
import com.piesat.util.ReturnCodeEnum;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.internal.DnsNameResolverProvider;
import io.grpc.netty.shaded.io.netty.util.NettyRuntime;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sun.java2d.pipe.SpanShapeRenderer;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-25 15:08
 **/
@RestController
@Api(value="迁移备份清除启停接口",tags = {"迁移备份清除启停接口"})
@RequestMapping("/schedule/job")
@Slf4j
public class JobInfoController {
    @Autowired
    private JobInfoService jobInfoService;
    @Autowired
    private DataBaseService dataBaseService;
    @Autowired
    private BackupService backupService;
    @Autowired
    private BackupMapstruct backupMapstruct;
    @Autowired
    private MoveMapstruct moveMapstruct;
    @Autowired
    private MoveService moveService;
    @Autowired
    private ClearMapstruct clearMapstruct;
    @Autowired
    private ClearService clearService;
    @Autowired
    private BackupDao backupDao;
    @Autowired
    private MoveDao moveDao;
    @Autowired
    private ClearDao clearDao;
    @Autowired
    private DiSendService diSendService;

    @GetMapping("/findAllDataBase")
    @ApiOperation(value = "查询所有物理库接口", notes = "查询所有物理库接口")
    public ResultT findAllDataBase(){
        ResultT resultT=new ResultT();
        List<DatabaseDto> databaseDtos= (List<DatabaseDto>) dataBaseService.findAllDataBase();
        resultT.setData(databaseDtos);
        return resultT;
    }
    @GetMapping(value = "/getByDatabaseId/{databaseId}")
    public ResultT getByDatabaseId(@PathVariable String databaseId){
        ResultT resultT=new ResultT();
        List<Map<String, Object>> mapList=dataBaseService.getByDatabaseId(databaseId);
        resultT.setData(mapList);
        return resultT;
    }
    @ApiOperation(value = "查询所有物理库详情接口", notes = "查询所有物理库详情接口")
    @GetMapping(value = "/findDataBaseById/{databaseId}")
    public  ResultT<DatabaseDto> findDataBaseById(@PathVariable String databaseId){
        ResultT<DatabaseDto> resultT=new ResultT();
        DatabaseDto databaseDto=dataBaseService.findDataBaseById(databaseId);
        resultT.setData(databaseDto);
        return resultT;
    }
    @GetMapping(value = "/getByDatabaseIdAndClassId")
    @ApiOperation(value = "查询资料详情接口", notes = "查询资料详情接口")
    public ResultT getByDatabaseIdAndClassId(String databaseId,String dataClassId){
        ResultT resultT=new ResultT();
        try {
            DataRetrieval dataRetrieval =dataBaseService.getByDatabaseIdAndClassId(databaseId,dataClassId);
            resultT.setData(dataRetrieval);
        } catch (Exception e) {
            resultT.setErrorMessage(ReturnCodeEnum.ReturnCodeEnum_601_ERROR);
        }
        return resultT;
    }
    @GetMapping(value = "/startById")
    @ApiOperation(value = "启动任务接口", notes = "启动任务接口")
    @RequiresPermissions("schedule:job:startById")
    public ResultT<String> startById(String id){
        ResultT resultT=new ResultT();
        try {
            jobInfoService.startById(id);
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage("启动失败");
        }
        return resultT;
    }
    @GetMapping(value = "/stop")
    @ApiOperation(value = "停止任务接口", notes = "停止任务接口")
    @RequiresPermissions("schedule:job:stop")
    public ResultT<String> stop(String id){
        ResultT resultT=new ResultT();
        try {
            jobInfoService.stopById(id);
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage("停止失败");
        }
        return resultT;
    }
    @GetMapping(value = "/execute")
    @RequiresPermissions("schedule:job:execute")
    @ApiOperation(value = "立即执行接口", notes = "立即执行接口")
    public ResultT<String> execute(String id){
        ResultT resultT=new ResultT();
        try {
            jobInfoService.execute(id);
        } catch (Exception e) {
            log.error(OwnException.get(e));
            resultT.setErrorMessage("立即执行失败");
        }
        return resultT;
    }

    @GetMapping(value = "/executeAll")
    @RequiresPermissions("schedule:job:execute")
    @ApiOperation(value = "立即执行所有", notes = "立即执行所有")
    public ResultT<String> executeAll(){
        ResultT resultT=new ResultT();
        try {
            List<JobInfoDto> jobInfoDtos=jobInfoService.findJobList();
            for(JobInfoDto jobInfoDto:jobInfoDtos){
                jobInfoService.execute(jobInfoDto.getId());
            }
        } catch (Exception e) {

            log.error(OwnException.get(e));
            resultT.setErrorMessage("立即执行失败");
        }
        return resultT;
    }
    @GetMapping(value = "/executeAllB")
    @RequiresPermissions("schedule:job:execute")
    @ApiOperation(value = "立即补偿所有", notes = "立即补偿所有")
    public ResultT<String> executeAllB(String time){
        ResultT resultT=new ResultT();
        try {
            List<JobInfoDto> jobInfoDtos=jobInfoService.findJobList();
            for(JobInfoDto jobInfoDto:jobInfoDtos){
                if(!"MMD".equals(jobInfoDto.getType())&&!"JOB".equals(jobInfoDto.getType())){
                    jobInfoService.executeB(jobInfoDto.getId(),time);
                }
            }
        } catch (Exception e) {

            log.error(OwnException.get(e));
            resultT.setErrorMessage("立即执行失败");
        }
        return resultT;
    }
    @GetMapping(value = "/findThread")
    @ApiOperation(value = "获取所有线程", notes = "获取所有线程")
    public ResultT<List<String>> findThread(){
        ResultT<List<String>> resultT=new ResultT<>();
        List<String> list=new ArrayList<>();
        ThreadGroup currentGroup = Thread.currentThread().getThreadGroup();
        int noThreads = currentGroup.activeCount();
        Thread[] lstThreads = new Thread[noThreads];
        currentGroup.enumerate(lstThreads);
        for (int i = 0; i < noThreads; i++){
            list.add("线程号：" + i + " = " + lstThreads[i].getName()+",线程状态="+ lstThreads[i].getState());

        }
        resultT.setData(list);
        return resultT;
    }
    @GetMapping(value = "/channel")
    @ApiOperation(value = "获取所有通道", notes = "获取所有通道")
    public ResultT<List<String>> channel(){
        ChannelUtil channelUtil=ChannelUtil.getInstance();
        ConcurrentHashMap<String, Channel> grpcChannel= channelUtil.getGrpcChannel();
        ResultT<List<String>> resultT=new ResultT<>();
        List<String> list=new ArrayList<>();
        grpcChannel.forEach((k,v)->list.add("Channel:"+k));
        list.add(String.valueOf(NettyRuntime.availableProcessors()));
        resultT.setData(list);
        return resultT;
    }
    @GetMapping(value = "/addchannel")
    @ApiOperation(value = "增加通道", notes = "增加通道")
    public ResultT<List<String>> addchannel(){

        ResultT<List<String>> resultT=new ResultT<>();
        List<String> list=new ArrayList<>();
        ManagedChannel channel = ManagedChannelBuilder.forAddress("10.40.17.35", 18058)
                .defaultLoadBalancingPolicy("round_robin")
                .nameResolverFactory(new DnsNameResolverProvider())
                .usePlaintext().build();
        resultT.setData(list);
        return resultT;
    }
    @GetMapping(value = "/findTimeZone")
    @ApiOperation(value = "获取时区", notes = "获取时区")
    public ResultT<String> findTimeZone(){

        ResultT<String> resultT=new ResultT<>();

        resultT.setData(TimeZone.getDefault().getID()+":"+System.currentTimeMillis());
        return resultT;
    }
    @GetMapping(value = "/sendDI")
    @ApiOperation(value = "发送di配置", notes = "发送di配置")
    public ResultT<String> sendDI(String isDelete) {
        List<BackupEntity> backupEntityList = backupDao.findAll();
        for (BackupEntity backupEntity : backupEntityList) {
            try {
                if(null!=isDelete&&isDelete.equals("true")) {
                    diSendService.sendDeleteDi(backupEntity.getId());
                }
                if(backupEntity.getTriggerStatus()!=1){
                    continue;
                }
                diSendService.sendBackup(backupEntity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<MoveEntity> moveEntityList = moveDao.findAll();
        for (MoveEntity moveDto : moveEntityList) {
            try {
                if(null!=isDelete&&isDelete.equals("true")) {
                    diSendService.sendDeleteDi(moveDto.getId());
                }
                if(moveDto.getTriggerStatus()!=1){
                    continue;
                }
                diSendService.sendMove(moveDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        List<ClearEntity> clearEntityList = clearDao.findAll();
        for (ClearEntity clearDto : clearEntityList) {
            try {
                if(null!=isDelete&&isDelete.equals("true")) {
                    diSendService.sendDeleteDi(clearDto.getId());
                }
                if(clearDto.getTriggerStatus()!=1){
                    continue;
                }
                diSendService.sendClear(clearDto);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return new ResultT<>();
    }

    @GetMapping(value = "/deleteDI")
    @ApiOperation(value = "删除di配置", notes = "删除di配置")
    public ResultT<String> deleteDI(String taskId) {
        diSendService.sendDeleteDi(taskId);
        return new ResultT<>();
    }
    @GetMapping(value = "/redisScan")
    @ApiOperation(value = "redis扫描", notes = "redis扫描")
    public ResultT<Long> redisScan(String key) {
        ResultT<Long> resultT=new ResultT<>();
        RedisUtil redisUtil= SpringUtil.getBean(RedisUtil.class);
        long size=redisUtil.scanSize(key);
        resultT.setData(size);
        return resultT;
    }
    @GetMapping(value = "/getNextTime")
    @ApiOperation(value = "计算下5次执行时间", notes = "计算下5次执行时间")
    public ResultT< List<String>> getNextTime(String cronExpression){
        ResultT< List<String>> resultT=new ResultT<>();
        List<String> cronTimeList = new ArrayList<>();
        try {
            CronSequenceGenerator cronSequenceGenerator = new CronSequenceGenerator(cronExpression);
            Date nextTimePoint = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < 5; i++) {
                nextTimePoint = cronSequenceGenerator.next(nextTimePoint);
                cronTimeList.add(sdf.format(nextTimePoint));
            }
            resultT.setData(cronTimeList);
        } catch (Exception e) {
            resultT.setErrorMessage("表达式错误");
            e.printStackTrace();
        }
        return resultT;
    }
}

