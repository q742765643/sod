package com.piesat.schedule.mapper.backup;

import com.piesat.schedule.entity.JobInfoLogEntity;
import com.piesat.schedule.entity.backup.BackupLogEntity;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-06 14:29
 **/
@Component
public interface BackupLogMapper {
    List<BackupLogEntity> findMaxBackupTime(String jobId);

    List<BackupLogEntity> findByJobId(BackupLogEntity backupLogEntity);
}

