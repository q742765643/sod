package com.piesat.dm.rpc.api.dataapply;

import com.piesat.dm.rpc.dto.dataapply.CloudDatabaseApplyDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/12 16:13
 */
public interface CloudDatabaseApplyService {

    PageBean selectPageList(PageForm<CloudDatabaseApplyDto> pageForm);

    CloudDatabaseApplyDto saveDto(CloudDatabaseApplyDto cloudDatabaseApplyDto);

    CloudDatabaseApplyDto updateDto(CloudDatabaseApplyDto cloudDatabaseApplyDto);

    CloudDatabaseApplyDto getDotById(String id);

    public void deleteById(String id);

    List<CloudDatabaseApplyDto> getByUserId(String userId);

    CloudDatabaseApplyDto updateExamineStatus(String id,String examineStatus);


}
