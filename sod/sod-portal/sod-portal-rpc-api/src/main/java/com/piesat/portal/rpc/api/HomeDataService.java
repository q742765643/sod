package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.HomeDataDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * Portal首页数据管理
 */
public interface HomeDataService {

    PageBean selectPageList(PageForm<HomeDataDto> pageForm);

    HomeDataDto saveDto(HomeDataDto homeDataDto);

    HomeDataDto updateDto(HomeDataDto homeDataDto);

    HomeDataDto getDotById(String id);

    void delete(String id);

}
