package com.piesat.schedule.rpc.api.synctofile;

import com.piesat.schedule.rpc.dto.synctofile.SyncToFileLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * @author cwh
 * @date 2020年 10月28日 16:04:24
 */
public interface SyncToFileLogService {
    /**
     * 按页查询
     * @param pageForm
     * @return
     */
    PageBean selectSyncToFileLogList(PageForm<SyncToFileLogDto> pageForm);

    /**
     * 根据作业id查询
     * @param jobId
     * @return
     */
    SyncToFileLogDto selectSyncToFileLogByJobId(String jobId);

    /**
     * 根据id查询
     * @param syncToFileLogId
     * @return
     */
    SyncToFileLogDto findSyncToFileLogById(String syncToFileLogId);

    /**
     * 删除
     * @param syncToFileLogIds
     */
    void deleteSyncToFileLogByIds(String[] syncToFileLogIds);

    public void exportExcel(SyncToFileLogDto synctofileLogDto);
}
