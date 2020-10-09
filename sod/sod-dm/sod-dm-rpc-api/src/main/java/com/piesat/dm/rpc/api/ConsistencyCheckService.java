package com.piesat.dm.rpc.api;

import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/14 10:42
 */
public interface ConsistencyCheckService {

    PageBean selectPageList(PageForm<ConsistencyCheckDto> pageForm);

    ConsistencyCheckDto saveDto(ConsistencyCheckDto consistencyCheckDto);

    void deleteById(String id);

    public void deleteRecordByIds(List<String> ids);

    Map<String, List<List<String>>>  downloadDfcheckFile(String databaseId);

    void updateEleInfo(String databaseId);
}
