package com.piesat.schedule.mapper.backup;

import com.piesat.schedule.entity.JobInfoLogEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import org.springframework.stereotype.Component;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-06 14:29
 **/
@Component
public interface BackupLogMapper {
    BackupLogEntity findMaxBackupTime(String jobId);

}

