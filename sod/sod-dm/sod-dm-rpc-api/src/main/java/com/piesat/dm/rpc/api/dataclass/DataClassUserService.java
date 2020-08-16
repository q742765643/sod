package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassUserDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 资料用户关系
 *
 * @author cwh
 * @date 2020年 07月31日 16:10:25
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassUserService {
    DataClassUserDto saveDto(DataClassUserDto DataClassUserDto);

    List<DataClassUserDto> saveList(List<DataClassUserDto> dataLogicList);

    void deleteByDataClassId(String dataclassId);

    List<DataClassUserDto> findByDataClassId(String dataclassId);


    List<DataClassUserDto> findByUserName(String userName);
}
