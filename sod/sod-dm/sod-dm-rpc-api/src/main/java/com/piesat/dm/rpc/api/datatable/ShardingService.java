package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.PartingDto;
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
    PartingDto saveDto(PartingDto partingDto);

    List<PartingDto> saveDto(List<PartingDto> partingDto);

    PartingDto getDotById(String id);

    PartingDto getDotByTableId(String id);

    void delete(String id);

    List<PartingDto> all();


}
