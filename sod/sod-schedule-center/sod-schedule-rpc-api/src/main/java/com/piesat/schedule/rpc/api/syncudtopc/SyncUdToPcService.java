package com.piesat.schedule.rpc.api.syncudtopc;

import com.piesat.schedule.rpc.dto.syncudtopc.SyncUdToPcDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @date 2020年 10月28日 15:01:11
 */
public interface SyncUdToPcService {
    /**
     * 分页查询
     * @param pageForm
     * @return
     */
    PageBean selectSyncUdToPcList(PageForm<SyncUdToPcDto> pageForm);
    /**
     * 按id查询
     * @param syncUdToPcId
     * @return
     */
    SyncUdToPcDto findSyncUdToPcById(String syncUdToPcId);

    /**
     * 保存
     * @param syncUdToPcDto
     */
    void saveSyncUdToPc(SyncUdToPcDto syncUdToPcDto);

    /**
     * 修改
     * @param syncUdToPcDto
     */
    void updateSyncUdToPc(SyncUdToPcDto syncUdToPcDto);

    /**
     * 删除
     * @param syncUdToPcIds
     */
    void deleteSyncUdToPcIds(String[] syncUdToPcIds);

    public List<Map<String,Object>> findDatabase();
    List<Map<String,Object>> findDataClassId(String dataBaseId, String dataClassId);
    //    导出
    public void exportExcel(SyncUdToPcDto syncUdToPcDto);

}
