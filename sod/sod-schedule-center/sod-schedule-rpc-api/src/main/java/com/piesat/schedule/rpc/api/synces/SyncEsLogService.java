package com.piesat.schedule.rpc.api.synces;

import com.piesat.schedule.rpc.dto.synces.SyncEsLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * @author cwh
 * @date 2020年 10月28日 16:01:59
 */
public interface SyncEsLogService {
    /**
     * 按页查询
     * @param pageForm
     * @return
     */
    PageBean selectSyncEsLogList(PageForm<SyncEsLogDto> pageForm);

    /**
     * 根据作业id查询
     * @param jobId
     * @return
     */
    SyncEsLogDto selectSyncEsLogByJobId(String jobId);

    /**
     * 根据id查询
     * @param syncEsLogId
     * @return
     */
    SyncEsLogDto findSyncEsLogById(String syncEsLogId);

    /**
     * 删除
     * @param syncEsLogIds
     */
    void deleteSyncEsLogByIds(String[] syncEsLogIds);
}
