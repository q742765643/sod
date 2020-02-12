package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DataServerConfigDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 服务信息配置
 *
 * @author cwh
 * @date 2020年 02月12日 15:55:33
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataServerConfigService {
    DataServerConfigDto saveDto(DataServerConfigDto dataServerConfigDto);

    DataServerConfigDto getDotById(String id);

    void delete(String id);

    List<DataServerConfigDto> all();
}
