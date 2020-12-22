package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
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
    DataTableInfoDto saveDto(DataTableInfoDto dataTableDto);

    DataTableInfoDto getDotById(String id);

    void delete(String id);

    List<DataTableInfoDto> all();

    List<DataTableInfoDto> getByDatabaseIdAndClassId(String databaseId, String dataClassId);

    List<Map<String, Object>> getByDatabaseId(String databaseId);

    List<Map<String, Object>> findByUserId(String userId);

    List<Map<String, Object>> getByClassId(String dataClassId);

    List<Map<String, Object>> getMultiDataInfoByClassId(String dataClassId);

    List<DataTableInfoDto> getByClassLogicId(String classLogic);

    ResultT getOverview(String databaseId, String dataClassId);

    ResultT getSampleData(SampleData sampleData) throws Exception;

    List<Map<String, Object>> getByDatabaseIdAndTableName(String databaseId, String tableName);

    ResultT paste(String copyId, String pasteId);

    ResultT createTable(TableSqlDto tableSqlDto);

    ResultT existTable(TableSqlDto tableSqlDto);

    /**
     * 按物理库查询要素表
     *
     * @param databaseId
     * @return
     */
    List<DataTableInfoDto> findETable(String databaseId);

    /**
     * 根据tableId查询相关的键表要素表
     *
     * @param tableId
     * @return
     */
    List<Map<String, Object>> findTables(String tableId);

    /**
     * 获取关联表信息
     * @param tableId
     * @return
     */
    List<Map<String, Object>> getRelatedTables(String tableId);

    /**
     * 对比字段信息
     *
     * @param dataTableInfoDto
     * @param resultT
     */
    void contrastColumns(DataTableInfoDto dataTableInfoDto, ResultT resultT);

}
