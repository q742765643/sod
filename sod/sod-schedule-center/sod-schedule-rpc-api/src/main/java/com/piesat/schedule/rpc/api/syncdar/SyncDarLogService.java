package com.piesat.schedule.rpc.api.syncdar;

import com.piesat.schedule.rpc.dto.syncdar.SyncDarLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * @author cwh
 * @date 2020年 10月28日 16:01:59
 */
public interface SyncDarLogService {
    /**
     * 按页查询
     * @param pageForm
     * @return
     */
    PageBean selectSyncDarLogList(PageForm<SyncDarLogDto> pageForm);

    /**
     * 根据作业id查询
     * @param jobId
     * @return
     */
    SyncDarLogDto selectSyncDarLogByJobId(String jobId);

    /**
     * 根据id查询
     * @param syncDarLogId
     * @return
     */
    SyncDarLogDto findSyncDarLogById(String syncDarLogId);

    /**
     * 删除
     * @param syncDarLogIds
     */
    void deleteSyncDarLogByIds(String[] syncDarLogIds);
}
