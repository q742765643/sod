package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DataServerBaseInfoDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 服务基础信息
 *
 * @author cwh
 * @date 2020年 02月12日 15:10:16
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataServerBaseInfoService {
    DataServerBaseInfoDto saveDto(DataServerBaseInfoDto dataServerBaseInfoDto);

    DataServerBaseInfoDto getDotById(String id);

    void delete(String id);

    List<DataServerBaseInfoDto> all();

    List<DataServerBaseInfoDto> findByDataCLassId(String dataCLassId);
}
