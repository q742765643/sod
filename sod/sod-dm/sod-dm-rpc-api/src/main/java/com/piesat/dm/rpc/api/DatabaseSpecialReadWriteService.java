package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.DatabaseSpecialReadWriteDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 专题库管理
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DatabaseSpecialReadWriteService {

    /**
     * 获取专题库资料列表
     * @param sdbId
     * @param dataType
     * @return
     */
    List<DatabaseSpecialReadWriteDto> getDotList(String sdbId, String dataType);
}
