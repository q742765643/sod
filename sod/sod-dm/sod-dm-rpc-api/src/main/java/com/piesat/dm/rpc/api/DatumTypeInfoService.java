package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DatumTypeInfoDto;

import java.util.List;

/**
 * 公共元数据
 *
 * @author cwh
 * @date 2019年 11月27日 16:20:43
 */
@GrpcHthtService(server = "database",serialization = SerializeType.PROTOSTUFF)
public interface DatumTypeInfoService {
    DatumTypeInfoDto saveDto(DatumTypeInfoDto datumTypeInfoDto);
    DatumTypeInfoDto getDotById(String id);
    void delete(String id);
    List<DatumTypeInfoDto> all();
}
