package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.TableIndexDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
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
}
