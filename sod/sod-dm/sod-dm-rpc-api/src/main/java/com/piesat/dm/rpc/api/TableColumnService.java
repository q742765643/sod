package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.TableColumnDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 表字段信息
 *
 * @author cwh
 * @date 2019年 11月22日 15:37:38
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface TableColumnService {
    TableColumnDto saveDto(TableColumnDto tableColumnDto);

    List<TableColumnDto> saveDtoList(List<TableColumnDto> tableColumnDtoList);

    TableColumnDto getDotById(String id);

    void delete(String id);

    List<TableColumnDto> all();

    List<TableColumnDto> findByTableId(String tableId);
}
