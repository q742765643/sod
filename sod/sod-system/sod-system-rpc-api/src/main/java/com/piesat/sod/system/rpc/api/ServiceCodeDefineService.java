package com.piesat.sod.system.rpc.api;

import com.piesat.sod.system.entity.ServiceCodeDefineEntity;
import com.piesat.sod.system.rpc.dto.ServiceCodeDefineDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 14:04
 */
public interface ServiceCodeDefineService {
    PageBean selectPageList(PageForm<ServiceCodeDefineDto> pageForm);

    ServiceCodeDefineDto saveDto(ServiceCodeDefineDto serviceCodeDefineDto);

    ServiceCodeDefineDto updateDto(ServiceCodeDefineDto serviceCodeDefineDto);

    public void deleteRecordByIds(List<String> ids);

    List<ServiceCodeDefineDto> findByDbFcstEle(String dbFcstEle);

    ServiceCodeDefineDto findById(String id);
}
