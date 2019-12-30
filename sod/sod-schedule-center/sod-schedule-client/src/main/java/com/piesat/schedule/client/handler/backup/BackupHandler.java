package com.piesat.schedule.client.handler.backup;

import com.piesat.schedule.client.handler.base.BaseHandler;
import com.piesat.schedule.entity.JobInfoEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:17
 */
@Slf4j
@Service("backupHandler")
public class BackupHandler implements BaseHandler {
    @Override
    public void execute(JobInfoEntity jobInfoEntity) {
        log.info("备份调用成功");

    }
}
