package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
import java.util.Map;

/**
 * 资料用途分类
 *
 * @author cwh
 * @date 2019年 11月22日 15:34:03
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataLogicService {
    DataClassLogicDto saveDto(DataClassLogicDto dataLogicDto);

    List<DataClassLogicDto> saveList(List<DataClassLogicDto> dataLogicList);

    DataClassLogicDto getDotById(String id);

    void delete(String id);

    void deleteById(String id);

    void onlyDeleteById(String id);

    List<DataClassLogicDto> all();

    List<Map<String, Object>> getByDataClassIds(List<String> ids);

    List<Map<String, Object>> getByDatabaseId(String databaseId);

    List<Map<String, Object>> getDistinctDatabaseAndLogic();

    void deleteByDataClassId(String dataClassId);

    List<DataClassLogicDto> findByDataClassId(String dataClassId);

    List<DataTableInfoDto> getDataLogic(String dataclassId, String databaseId, String tableName);

    List<DataClassLogicDto> findByTableId(String tableId);

    Map<String, Object> getTableByDBLogics(String tdbId, String logics);

    Map<String,List<String>> getDatabaseTables(String logics);
}
