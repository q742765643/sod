package com.piesat.portal.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.portal.rpc.dto.DynManageDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * 业务动态管理
 */
public interface DynManageService {

    PageBean selectPageList(PageForm<DynManageDto> pageForm);

    DynManageDto saveDto(DynManageDto dynManageDto);

    DynManageDto updateDto(DynManageDto dynManageDto);

    public void deleteRecordByIds(List<String> ids);

    DynManageDto getDotById(String id);

    void delete(String id);
}
