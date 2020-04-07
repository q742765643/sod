package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.LogicStorageTypesDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 数据用途与存储类型对应关系
 *
 * @author cwh
 * @date 2019年 11月22日 15:36:13
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface LogicStorageTypesService {
    LogicStorageTypesDto saveDto(LogicStorageTypesDto logicStorageTypesDto);

    LogicStorageTypesDto getDotById(String id);

    void delete(String id);

    List<LogicStorageTypesDto> all();
}
