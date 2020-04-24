package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.util.constant.GrpcConstant;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 表结构导出
 *
 * @author wulei
 * @date 2019年 11月22日 15:38:16
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface TableExportService {
    /**
     * 获取导出的数据结构
     * @return
     */
    Map<String, Object> getExportMap(String physicalDBParam, String dataClassIdParam);

    /**
     * 查询逻辑库名称
     * @return
     */
    String getLogicName(String database_logic);

    /**
     * 获取物理库名称
     * @return
     */
    String getDatabaseName(String physical_id);

    /**
     * 导出sql文件
     * @param databaseId
     * @param dataClassIds
     * @param exportType
     * @return
     */
    Map<String, Object> exportSqlFile(String databaseId, String dataClassIds, Integer exportType,String outFilePath);

    /**
     * 导出表结构-简版
     * @param database_id
     * @param data_class_ids
     * @return
     */
    Map<String, Object> getExportMapSimple(String database_id, String data_class_ids, String columns, String index, String shard);
}
