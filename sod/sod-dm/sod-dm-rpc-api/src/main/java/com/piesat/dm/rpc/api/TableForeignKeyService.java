package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.TableForeignKeyDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 数据库外键关联
 *
 * @author cwh
 * @date 2019年 12月09日 14:04:10
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface TableForeignKeyService {
    TableForeignKeyDto saveDto(TableForeignKeyDto tableForeignKeyDto);

    TableForeignKeyDto getDotById(String id);

    void delete(String id);

    List<TableForeignKeyDto> all();

    List<TableForeignKeyDto> findByTableId(String tableId);
}
