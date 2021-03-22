package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassLabelDefDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassLabelDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassNormDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 数据标签
 *
 * @author cwh
 * @date 2020年 07月31日 16:10:25
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassLabelDefService {
    /**
     * 保存
     * @param dataClassLabelDefDto
     * @return
     */
    DataClassLabelDefDto saveDto(DataClassLabelDefDto dataClassLabelDefDto);

    /**
     * 根据id获取
     * @param id
     * @return
     */
    DataClassLabelDefDto getDotById(String id);

    /**
     * 根据id删除
     * @param id
     */
    void delete(String id);

    /**
     * 查下所有
     * @return
     */
    List<DataClassLabelDefDto> all();
}
