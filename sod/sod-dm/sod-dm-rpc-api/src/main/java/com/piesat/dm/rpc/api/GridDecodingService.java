package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.GridDecodingDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 解码配置
 *
 * @author cwh
 * @date 2020年 02月12日 11:39:24
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface GridDecodingService {
    GridDecodingDto saveDto(GridDecodingDto gridDecodingDto);

    GridDecodingDto getDotById(String id);

    void delete(String id);

    List<GridDecodingDto> all();
}
