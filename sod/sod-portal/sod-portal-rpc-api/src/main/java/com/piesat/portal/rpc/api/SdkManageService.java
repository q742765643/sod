package com.piesat.portal.rpc.api;

import com.piesat.portal.rpc.dto.SdkManageDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * SDK管理
 */
public interface SdkManageService {

    PageBean selectPageList(PageForm<SdkManageDto> pageForm);

    void delete(String id);

    SdkManageDto getDotById(String id);

    SdkManageDto saveDto(SdkManageDto sdkManageDto);

    SdkManageDto updateDto(SdkManageDto sdkManageDto);
}
