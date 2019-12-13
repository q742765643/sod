package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DataLogicDto;

import java.util.List;

/**
 * 资料用途分类
 *
 * @author cwh
 * @date 2019年 11月22日 15:34:03
 */
@GrpcHthtService(server = "database",serialization = SerializeType.PROTOSTUFF)
public interface DataLogicService {
    DataLogicDto saveDto(DataLogicDto dataLogicDto);
    DataLogicDto getDotById(String id);
    void delete(String id);
    List<DataLogicDto> all();
}
