package com.piesat.schedule.rpc.api.backup;

import com.piesat.schedule.rpc.dto.backup.BackupLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * Created by zzj on 2019/12/29.
 */
public interface BackupLogService {
    public PageBean selectBackupLogList(PageForm<BackupLogDto> pageForm);

    public BackupLogDto selectBackupLoByJobId(String jobId);

    public BackupLogDto findBackupLogById(String backupLogId);

    public void deleteBackupLogByIds(String[] backupLogIds);

    public void exportExcel(BackupLogDto backupLogDto);
}
