package com.piesat.schedule.sync.client.handler.synces;

import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.synces.SyncEsEntity;
import com.piesat.schedule.sync.client.handler.base.BaseHandler;
import com.piesat.util.ResultT;
import com.piesat.schedule.sync.client.Service.synces.SyncEsLogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 非结构化数据交换系统同步
 * @author cwh
 * @date 2020年 10月29日 14:12:00
 */
@Slf4j
@Service("syncEsHandler")
public class SyncEsHandler implements BaseHandler {

    @Autowired
    private SyncEsLogService syncEsLogService;

    @Override
    public void execute(JobInfoEntity jobInfoEntity, ResultT<String> resultT) {
        log.info("非结构化数据交换系统同步调用成功");
        SyncEsEntity syncEsEntity = (SyncEsEntity) jobInfoEntity;
        long occurTime = System.currentTimeMillis();
    }
}
