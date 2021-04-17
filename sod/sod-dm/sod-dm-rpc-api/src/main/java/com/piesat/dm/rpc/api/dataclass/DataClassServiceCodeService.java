package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassServiceCodeDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassServiceCodeList;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * @author cuiwenhui
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassServiceCodeService {

    /**
     * 保存
     *
     * @param dataClassServiceCodeDto
     * @return
     */
    DataClassServiceCodeDto saveDto(DataClassServiceCodeDto dataClassServiceCodeDto);

    List<DataClassServiceCodeDto> saveDtoList(List<DataClassServiceCodeDto> dataClassServiceCodeList);

    /**
     * 获取
     *
     * @param id
     * @return
     */
    DataClassServiceCodeDto getDotById(String id);

    /**
     * 删除
     *
     * @param id
     */
    void deleteById(String id);

    /**
     * 按存储编码删除
     * @param classId
     */
    void deleteByClassId(String classId);

    /**
     * 查询所有
     *
     * @return
     */
    List<DataClassServiceCodeDto> all();

    List<DataClassServiceCodeDto> getServiceCode(String tableId, String dataclassId);
}