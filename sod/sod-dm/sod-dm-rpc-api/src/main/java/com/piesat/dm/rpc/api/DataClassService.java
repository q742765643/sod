package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
import java.util.Map;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月22日 15:33:10
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassService {
    DataClassDto saveDto(DataClassDto dataClassDto);

    DataClassDto getDotById(String id);

    void delete(String id);

    List<DataClassDto> all();

    List<Map<String, Object>> getLogicClass();

    List<Map<String, Object>> getDatabaseClass();
}
