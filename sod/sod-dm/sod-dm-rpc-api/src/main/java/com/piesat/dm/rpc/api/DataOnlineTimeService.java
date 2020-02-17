package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DataOnlineTimeDto;
import com.piesat.util.constant.GrpcConstant;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/17 19:19
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataOnlineTimeService {

    DataOnlineTimeDto saveDto(DataOnlineTimeDto dataOnlineTimeDto);

    void deleteByDataClassId(String dataClassId);

}
