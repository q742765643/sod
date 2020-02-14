package com.piesat.dm.rpc.api;

import com.piesat.dm.rpc.dto.ConsistencyCheckDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/14 10:42
 */
public interface ConsistencyCheckService {
    PageBean selectPageList(PageForm<ConsistencyCheckDto> pageForm);
}
