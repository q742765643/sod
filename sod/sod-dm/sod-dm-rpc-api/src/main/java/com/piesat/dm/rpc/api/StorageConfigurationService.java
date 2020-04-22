package com.piesat.dm.rpc.api;

import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:28
 */
public interface StorageConfigurationService {

    public PageBean selectPageList(PageForm<Map<String,String>> pageForm);

    public PageBean storageFieldList(PageForm<Map<String,String>> pageForm);

    StorageConfigurationDto saveDto(StorageConfigurationDto storageConfigurationDto);

    Map<String,Object> exportTable(Map<String,String> map);

    List<Map<String,Object>> findByDataClassId(String dataClassId);

    void deleteByDataClassId(String dataClassId);

    void updateDataAuthorityConfig(StorageConfigurationDto storageConfigurationDto);

    List<StorageConfigurationDto> findByClassLogicId(String id);
}
