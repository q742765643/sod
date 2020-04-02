package com.piesat.dm.rpc.api.special;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialAuthorityDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 专题库管理
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DatabaseSpecialAuthorityService {

    /**
     * 获取专题库对应的数据库权限
     * @param sdbId
     * @return
     */
    List<DatabaseSpecialAuthorityDto> getAuthorityBySdbId(String sdbId);
}
