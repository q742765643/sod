package com.piesat.dm.rpc.api;

import com.piesat.dm.rpc.dto.StorageConfigurationDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/9 15:28
 */
public interface StorageConfigurationService {

    public PageBean selectPageList(PageForm<Map<String,String>> pageForm);

    StorageConfigurationDto saveDto(StorageConfigurationDto storageConfigurationDto);

    Map<String,Object> exportTable(Map<String,String> map);

}
