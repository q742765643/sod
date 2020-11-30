package com.piesat.schedule.rpc.api.synces;

import com.piesat.schedule.rpc.dto.synces.SyncEsDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * @author cwh
 * @date 2020年 10月28日 14:59:13
 */
public interface SyncEsService {
    /**
     * 分页查询
     * @param pageForm
     * @return
     */
    PageBean selectSyncEsList(PageForm<SyncEsDto> pageForm);
    /**
     * 按id查询
     * @param syncEsId
     * @return
     */
    SyncEsDto findSyncEsById(String syncEsId);

    /**
     * 保存
     * @param syncEsDto
     */
    void saveSyncEs(SyncEsDto syncEsDto);

    /**
     * 修改
     * @param syncEsDto
     */
    void updateSyncEs(SyncEsDto syncEsDto);

    /**
     * 删除
     * @param syncEsIds
     */
    void deleteSyncEsIds(String[] syncEsIds);

}
