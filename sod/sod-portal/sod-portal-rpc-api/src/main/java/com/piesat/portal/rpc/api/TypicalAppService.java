package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.TypicalAppDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * 典型应用
 */
public interface TypicalAppService {

    PageBean selectPageList(PageForm<TypicalAppDto> pageForm);

    TypicalAppDto getDotById(String id);

    TypicalAppDto saveDto(TypicalAppDto typicalAppDto);

    void delete(String id);

    TypicalAppDto updateDto(TypicalAppDto typicalAppDto);

}
