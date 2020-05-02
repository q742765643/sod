package com.piesat.dm.rpc.api;

import com.piesat.dm.rpc.dto.ConsistencyCheckHistoryDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/16 16:13
 */
public interface ConsistencyCheckHistoryService {
    PageBean selectPageList(PageForm<ConsistencyCheckHistoryDto> pageForm);

    List<ConsistencyCheckHistoryDto> findHistoryByDatabaseIdAndFileName(String databaseId, String fileName);

    ConsistencyCheckHistoryDto saveDto(ConsistencyCheckHistoryDto consistencyCheckHistoryDto);

    void deleteById(String id);
}
