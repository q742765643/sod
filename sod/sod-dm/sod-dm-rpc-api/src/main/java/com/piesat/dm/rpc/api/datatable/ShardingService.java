package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.TablePartDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 分库分表键
 *
 * @author cwh
 * @date 2019年 11月22日 15:37:38
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface ShardingService {
    TablePartDto saveDto(TablePartDto tablePartDto);

    List<TablePartDto> saveDto(List<TablePartDto> tablePartDto);

    TablePartDto getDotById(String id);

    TablePartDto getDotByTableId(String id);

    void delete(String id);

    List<TablePartDto> all();


}
