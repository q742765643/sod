package com.piesat.schedule.sync.client.handler.syncdar;

import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.syncdar.SyncDarEntity;
import com.piesat.schedule.sync.client.Service.syncdar.SyncDarLogService;
import com.piesat.schedule.sync.client.handler.base.BaseHandler;
import com.piesat.util.ResultT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 非结构化数据交换系统同步
 * @author cwh
 * @date 2020年 10月29日 14:12:00
 */
@Slf4j
@Service("syncDarHandler")
public class SyncDarHandler implements BaseHandler {

    @Autowired
    private SyncDarLogService syncDarLogService;

    @Override
    public void execute(JobInfoEntity jobInfoEntity, ResultT<String> resultT) {
        log.info("非结构化数据交换系统同步调用成功");
        SyncDarEntity syncDarEntity = (SyncDarEntity) jobInfoEntity;
        long occurTime = System.currentTimeMillis();
    }
}
