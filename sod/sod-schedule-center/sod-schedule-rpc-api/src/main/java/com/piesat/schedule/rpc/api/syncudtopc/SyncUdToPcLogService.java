package com.piesat.schedule.rpc.api.syncudtopc;

import com.piesat.schedule.rpc.dto.syncudtopc.SyncUdToPcLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * @author cwh
 * @date 2020年 10月28日 16:08:43
 */
public interface SyncUdToPcLogService {
    /**
     * 按页查询
     * @param pageForm
     * @return
     */
    PageBean selectSyncUdToPcLogList(PageForm<SyncUdToPcLogDto> pageForm);

    /**
     * 根据作业id查询
     * @param jobId
     * @return
     */
    SyncUdToPcLogDto selectSyncUdToPcLogByJobId(String jobId);

    /**
     * 根据id查询
     * @param syncUdToPcLogId
     * @return
     */
    SyncUdToPcLogDto findSyncUdToPcLogById(String syncUdToPcLogId);

    /**
     * 删除
     * @param syncUdToPcLogIds
     */
    void deleteSyncUdToPcLogByIds(String[] syncUdToPcLogIds);

    public void exportExcel(SyncUdToPcLogDto syncUdToPcLogDto);
}
