package com.piesat.schedule.rpc.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.piesat.common.grpc.annotation.GrpcHthtClient;
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
import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.schedule.rpc.dto.clear.ClearDto;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.schedule.rpc.lock.RedisLock;
import com.piesat.schedule.rpc.mapstruct.backup.BackupMapstruct;
import com.piesat.schedule.rpc.mapstruct.clear.ClearMapstruct;
import com.piesat.schedule.rpc.mapstruct.move.MoveMapstruct;
import com.piesat.schedule.rpc.thread.ScheduleThread;
import com.piesat.schedule.rpc.thread.SendThread;
import com.piesat.sso.client.util.RedisUtil;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-19 17:26
 **/
@Component
public class ScheduleConfig implements ApplicationRunner {
    @Autowired
    private ScheduleThread scheduleThread;
    @Autowired
    private SendThread sendThread;
    private static ScheduledExecutorService timingPool;
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
    private RedisUtil redisUtil;
    @Autowired
    private RedisLock redisLock;
    @Override
    public void run(ApplicationArguments applicationArguments) throws Exception {

        sendThread.init();
        scheduleThread.start();
        ThreadFactory timingPoolFactory = new ThreadFactoryBuilder().setNameFormat("worker-timing-pool-%d").build();
        timingPool = Executors.newScheduledThreadPool(1, timingPoolFactory);
        //long nowTime=System.currentTimeMillis();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND,0);
        long nextTime =calendar.getTime().getTime();
        System.out.println("下次执行时间:"+nextTime);

        timingPool.scheduleWithFixedDelay (()->{
            boolean flag=redisLock.lock("init_job");
            if(flag){
                try {
                    List<BackupEntity> backupEntityList = backupDao.findAll();
                    List<BackUpDto> backUpDtoList = backupMapstruct.toDto(backupEntityList);
                    for (BackUpDto backUpDto : backUpDtoList) {
                        try {
                            backupService.updateBackup(backUpDto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    List<MoveEntity> moveEntityList = moveDao.findAll();
                    List<MoveDto> moveDtoList = moveMapstruct.toDto(moveEntityList);
                    for (MoveDto moveDto : moveDtoList) {
                        try {
                            moveService.updateMove(moveDto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    List<ClearEntity> clearEntityList = clearDao.findAll();
                    List<ClearDto> clearDtoList = clearMapstruct.toDto(clearEntityList);
                    for (ClearDto clearDto : clearDtoList) {
                        try {
                            clearService.updateClear(clearDto);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }finally {
                    redisLock.delete("init_job");
                }
            }

        },nextTime-System.currentTimeMillis(), 86400000, TimeUnit.MILLISECONDS);
    }
}

