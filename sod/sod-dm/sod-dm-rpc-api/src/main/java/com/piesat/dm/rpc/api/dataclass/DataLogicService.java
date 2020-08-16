package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
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
    DataLogicDto saveDto(DataLogicDto dataLogicDto);

    List<DataLogicDto> saveList(List<DataLogicDto> dataLogicList);

    DataLogicDto getDotById(String id);

    void delete(String id);

    void deleteById(String id);

    void onlyDeleteById(String id);

    List<DataLogicDto> all();

    List<Map<String, Object>> getByDataClassIds(List<String> ids);

    List<Map<String, Object>> getByDatabaseId(String databaseId);

    List<Map<String, Object>> getDistinctDatabaseAndLogic();

    void deleteByDataClassId(String dataClassId);

    List<DataLogicDto> findByDataClassIdAndLogicFlagAndStorageType(DataLogicDto dataLogicDto);


    List<DataLogicDto> findByDataClassId(String dataClassId);

    List<DataLogicDto> getDataLogic(String dataclassId,String databaseId,String tableName);

    Map<String, Object> getTableByDBLogics(String tdbId, String logics);
}
