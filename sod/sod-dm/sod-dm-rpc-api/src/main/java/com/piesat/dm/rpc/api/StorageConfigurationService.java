package com.piesat.dm.rpc.api;

import com.piesat.dm.rpc.dto.AdvancedConfigDto;
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

    PageBean selectPageList(PageForm<Map<String,String>> pageForm);

    PageBean storageFieldList(PageForm<Map<String,String>> pageForm);

    AdvancedConfigDto saveDto(AdvancedConfigDto advancedConfigDto);

    Map<String,Object> exportTable(Map<String,String> map);

    List<Map<String,Object>> findByDataClassId(String dataClassId);

    void deleteByDataClassId(String dataClassId);

    void updateDataAuthorityConfig(AdvancedConfigDto advancedConfigDto);

    List<AdvancedConfigDto> findByTableId(String id);
}
