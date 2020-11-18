package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.DplPlateDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * 算法板块管理
 */
public interface DplPlateService {

    PageBean selectPageList(PageForm<DplPlateDto> pageForm);

    DplPlateDto getDotById(String id);

    DplPlateDto saveDto(DplPlateDto dplPlateDto);

    void delete(String id);

    DplPlateDto updateDto(DplPlateDto dplPlateDto);
}
