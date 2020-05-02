package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassBaseInfoDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 资料基础信息
 *
 * @author cwh
 * @date 2020年 04月01日 15:49:03
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassBaseInfoService {
    DataClassBaseInfoDto saveDto(DataClassBaseInfoDto dataClassBaseInfoDto);

    DataClassBaseInfoDto getDotById(String id);

    void delete(String id);

    List<DataClassBaseInfoDto> all();

    DataClassBaseInfoDto getDataClassBaseInfo(String id);

    DataClassBaseInfoDto getAllDataClassBaseInfo(String id);

    DataClassBaseInfoDto saveDataClassBaseInfo(DataClassBaseInfoDto dataClassBaseInfoDto);
}
