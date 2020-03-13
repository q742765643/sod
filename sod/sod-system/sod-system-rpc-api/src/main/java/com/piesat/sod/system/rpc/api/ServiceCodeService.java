package com.piesat.sod.system.rpc.api;

import com.piesat.sod.system.rpc.dto.ServiceCodeDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/13 15:14
 */
public interface ServiceCodeService {

    PageBean selectPageList(PageForm<ServiceCodeDto> pageForm);

    ServiceCodeDto saveDto(ServiceCodeDto serviceCodeDto);

    ServiceCodeDto updateDto(ServiceCodeDto serviceCodeDto);

    ServiceCodeDto getDotById(String id);

    public void deleteRecordByIds(List<String> ids);

    public void deleteById(String id);

    List<ServiceCodeDto> queryAll();
}
