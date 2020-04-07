package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.TableSqlDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 表sql
 *
 * @author cwh
 * @date 2020年 04月02日 14:06:59
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface TableSqlService {
    TableSqlDto saveDto(TableSqlDto tableSqlDto);

    TableSqlDto getDotById(String id);

    void delete(String id);

    List<TableSqlDto> all();
}
