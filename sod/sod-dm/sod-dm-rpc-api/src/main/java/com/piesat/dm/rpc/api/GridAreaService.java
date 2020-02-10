package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.GridAreaDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 区域信息
 *
 * @author cwh
 * @date 2020年 02月10日 14:28:39
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface GridAreaService {
    GridAreaDto saveDto(GridAreaDto gridAreaDto);

    GridAreaDto getDotById(String id);

    void delete(String id);

    List<GridAreaDto> all();
}
