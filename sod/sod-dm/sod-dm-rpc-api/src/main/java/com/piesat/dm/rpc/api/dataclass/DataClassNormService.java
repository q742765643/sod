package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassNormDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 规范信息
 *
 * @author cwh
 * @date 2020年 02月12日 16:58:21
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassNormService {
    DataClassNormDto saveDto(DataClassNormDto dataClassNormDto);

    DataClassNormDto getDotById(String id);

    void delete(String id);

    List<DataClassNormDto> all();
}
