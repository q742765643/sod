package com.piesat.dm.rpc.api.special;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.special.DatabaseSpecialReadWriteDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
import java.util.Map;

/**
 * 专题库管理
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DatabaseSpecialReadWriteService {

    /**
     * 获取专题库资料列表
     * @param sdbId
     * @param dataType
     * @return
     */
    List<DatabaseSpecialReadWriteDto> getDotList(String sdbId, String dataType);

    List<DatabaseSpecialReadWriteDto> findBySdbIdAndDataClassId(String sdbId, String dataClassId);

    DatabaseSpecialReadWriteDto saveDto(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto);

    List<Map<String,Object>> getRecordSpecialByTdbId(String sdbId,String userId);
}
