package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.TableForeignKeyDto;
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

    int deleteByIdIn(List<String> ids);

    List<TableForeignKeyDto> all();

    List<TableForeignKeyDto> findByClassLogicId(String classLogicId);
}
