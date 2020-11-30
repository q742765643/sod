package com.piesat.schedule.rpc.api.syncdar;

import com.piesat.schedule.rpc.dto.syncdar.SyncDarDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @date 2020年 10月28日 14:59:13
 */
public interface SyncDarService {
    /**
     * 分页查询
     * @param pageForm
     * @return
     */
    PageBean selectSyncDarList(PageForm<SyncDarDto> pageForm);
    /**
     * 按id查询
     * @param syncDarId
     * @return
     */
    SyncDarDto findSyncDarById(String syncDarId);

    /**
     * 保存
     * @param syncDarDto
     */
    void saveSyncDar(SyncDarDto syncDarDto);

    /**
     * 修改
     * @param syncDarDto
     */
    void updateSyncDar(SyncDarDto syncDarDto);

    /**
     * 删除
     * @param syncDarIds
     */
    void deleteSyncDarIds(String[] syncDarIds);

    List<Map<String,Object>> findDataClassId(String dataBaseId, String dataClassId);

}
