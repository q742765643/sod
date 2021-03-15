package com.piesat.dm.rpc.api.dataapply;

import com.piesat.dm.rpc.dto.dataapply.DataResourceApplyDto;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/30 13:09
 */
public interface DataResourceApplyService {

    DataResourceApplyDto saveDto(DataResourceApplyDto dataResourceApplyDto);

    List<Map<String, Object>> findAuthorityList(String dataclassId, String databaseId);

}
