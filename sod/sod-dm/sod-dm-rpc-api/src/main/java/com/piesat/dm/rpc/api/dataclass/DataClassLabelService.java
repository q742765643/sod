package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.entity.dataclass.DataClassLabelEntity;
import com.piesat.dm.rpc.dto.dataclass.DataClassLabelDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 数据标签
 *
 * @author cwh
 * @date 2020年 07月31日 16:10:25
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassLabelService {
    DataClassLabelDto saveDto(DataClassLabelDto dataClassLabelDto);

    List<DataClassLabelDto> saveList(List<DataClassLabelDto> dataLogicList);

    void deleteByDataClassId(String dataclassId);

    List<DataClassLabelDto> findByDataClassId(String dataclassId);
}
