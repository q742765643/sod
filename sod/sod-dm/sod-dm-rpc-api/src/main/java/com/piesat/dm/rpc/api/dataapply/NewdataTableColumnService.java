package com.piesat.dm.rpc.api.dataapply;

import com.piesat.dm.rpc.dto.dataapply.NewdataTableColumnDto;

/**
 * @author yaya
 * @description TODO
 * @date 2020/4/18 21:32
 */
public interface NewdataTableColumnService {
    NewdataTableColumnDto saveDto(NewdataTableColumnDto newdataTableColumnDto);

    void deleteByApplyId(String applyId);
}
