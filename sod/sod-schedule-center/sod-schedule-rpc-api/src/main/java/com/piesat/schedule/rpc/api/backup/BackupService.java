package com.piesat.schedule.rpc.api.backup;

import com.piesat.schedule.rpc.dto.backup.BackUpDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * Created by zzj on 2019/12/23.
 */
public interface BackupService {
    public PageBean selectBackupList(PageForm<BackUpDto> pageForm);
    public BackUpDto findBackupById(String backupId);
    public void saveBackup(BackUpDto backUpDto);
    public void updateBackup(BackUpDto backUpDto);
    public void deleteBackupByIds(String[] backupIds);
}
