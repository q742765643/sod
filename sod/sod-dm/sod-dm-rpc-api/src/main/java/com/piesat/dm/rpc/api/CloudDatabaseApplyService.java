package com.piesat.dm.rpc.api;

import com.piesat.dm.rpc.dto.CloudDatabaseApplyDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/12 16:13
 */
public interface CloudDatabaseApplyService {

    PageBean selectPageList(PageForm<CloudDatabaseApplyDto> pageForm);

    CloudDatabaseApplyDto saveDto(CloudDatabaseApplyDto cloudDatabaseApplyDto);

    CloudDatabaseApplyDto updateDto(CloudDatabaseApplyDto cloudDatabaseApplyDto);

    public void deleteById(String id);


}
