package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.dto.datatable.SampleData;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

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
    List<Map<String, Object>> findETable(String databaseId);

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

    /**
     * 分页查询
     * @param pageForm
     * @return
     */
    PageBean getPageTableInfo(PageForm<Map<String, String>> pageForm);

    /**
     * 单表数据量
     * @param tableSqlDto
     * @return
     */
    long countTable(TableSqlDto tableSqlDto);

    /**
     * 根据子表类型查询
     * @param tableType
     * @param storageType
     * @return
     */
    List<Map<String, Object>> findBySubType(String tableType,String storageType);

    /**
     * 根据表名查询相关表信息
     * @param tableName
     * @return
     */
    List<Map<String, Object>> findTablesByTableName(String tableName);

    /**
     * 查询所有要素表
     * @return
     */
    List<Map<String, Object>> findAllETables();
}
