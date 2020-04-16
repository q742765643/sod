package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.SampleData;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.util.ResultT;
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

    void deleteByClassLogicId(String classLogicId);

    List<DataTableDto> all();

    List<DataTableDto> getByDatabaseIdAndClassId(String databaseId,String dataClassId);

    List<Map<String, Object>> getByDatabaseId(String databaseId);

    List<Map<String, Object>> findByUserId(String userId);

    List<Map<String, Object>> getByClassId(String dataClassId);

    List<Map<String, Object>> getMultiDataInfoByClassId(String dataClassId);

    List<DataTableDto> getByClassLogicId(String classLogic);


    int updateById(DataTableDto dataTableDto);

    ResultT getOverview(String databaseId,String dataClassId);

    ResultT getSampleData(SampleData sampleData) throws Exception;

    List<Map<String, Object>> getByDatabaseIdAndTableName(String databaseId,String tableName);

    ResultT paste(String copyId,String pasteId);

    ResultT createTable(TableSqlDto tableSqlDto);

    ResultT existTable(TableSqlDto tableSqlDto);

    List<DataTableDto> findByTableNameAndDatabaseIdAndDataclassId(String tableName,String databaseId,String dataclassId);
}
