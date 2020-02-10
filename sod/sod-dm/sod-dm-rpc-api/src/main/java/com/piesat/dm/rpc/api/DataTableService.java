package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
import java.util.Map;

/**
 * 表信息
 *
 * @author cwh
 * @date 2019年 11月22日 15:34:48
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataTableService {
    DataTableDto saveDto(DataTableDto dataTableDto);

    DataTableDto getDotById(String id);

    void delete(String id);

    List<DataTableDto> all();

    List<DataTableDto> getByDatabaseIdAndClassId(String databaseId,String dataClassId);
    List<Map<String, Object>> getByDatabaseId(String databaseId);

    List<DataTableDto> getByClassLogicId(String classLogic);

    Map<String,String> getSql(String tableId,String databaseId);
}
