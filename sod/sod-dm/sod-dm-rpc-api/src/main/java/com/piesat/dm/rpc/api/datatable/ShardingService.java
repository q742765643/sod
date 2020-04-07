package com.piesat.dm.rpc.api.datatable;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.datatable.ShardingDto;
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
    ShardingDto saveDto(ShardingDto shardingDto);

    List<ShardingDto> saveDto(List<ShardingDto> shardingDto);

    ShardingDto getDotById(String id);

    List<ShardingDto> getDotByTableId(String id);

    void delete(String id);

    List<ShardingDto> all();


}
