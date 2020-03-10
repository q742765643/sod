package com.piesat.schedule.rpc.api.backup;

import com.piesat.schedule.rpc.dto.backup.MetaBackupLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-02 09:56
 **/
public interface MetaBackupLogService {
    public PageBean selectMetaBackupLogList(PageForm<MetaBackupLogDto> pageForm);

    public MetaBackupLogDto findMetaBackupLogById(String metaBackupLogId);

    public void deleteMetaBackupLogByIds(String[] metaBackupLogIds);

    public void exportExcel(MetaBackupLogDto metaBackupLogDto);
}

