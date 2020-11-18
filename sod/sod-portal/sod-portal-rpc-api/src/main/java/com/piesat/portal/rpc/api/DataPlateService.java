package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.DataPlateDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * 数据板块管理
 */
public interface DataPlateService {

    PageBean selectPageList(PageForm<DataPlateDto> pageForm);

    DataPlateDto getDotById(String id);

    DataPlateDto saveDto(DataPlateDto dataPlateDto);

    void delete(String id);

    DataPlateDto updateDto(DataPlateDto dataPlateDto);

}
