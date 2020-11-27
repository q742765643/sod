package com.piesat.schedule.rpc.api.synctofile;

import com.piesat.schedule.rpc.dto.synctofile.SyncToFileDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @author cwh
 * @date 2020年 10月28日 15:00:47
 */
public interface SyncToFileService {
    /**
     * 分页查询
     * @param pageForm
     * @return
     */
    PageBean selectSyncToFileList(PageForm<SyncToFileDto> pageForm);
    /**
     * 按id查询
     * @param syncToFileId
     * @return
     */
    SyncToFileDto findSyncToFileById(String syncToFileId);

    /**
     * 保存
     * @param syncToFileDto
     */
    void saveSyncToFile(SyncToFileDto syncToFileDto);

    /**
     * 修改
     * @param syncToFileDto
     */
    void updateSyncToFile(SyncToFileDto syncToFileDto);

    /**
     * 删除
     * @param syncToFileIds
     */
    void deleteSyncToFileIds(String[] syncToFileIds);

    List<Map<String,Object>> findDataClassId(String dataBaseId, String dataClassId);

}
