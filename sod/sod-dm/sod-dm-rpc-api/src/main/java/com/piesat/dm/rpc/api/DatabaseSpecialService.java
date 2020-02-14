package com.piesat.dm.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DatabaseDto;
import com.piesat.dm.rpc.dto.DatabaseSpecialDto;
import com.piesat.dm.rpc.dto.DatabaseSpecialReadWriteDto;
import com.piesat.dm.rpc.dto.DatabaseUserDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;

/**
 * 专题库管理
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DatabaseSpecialService {

    /**
     * 查询所有
     * @return
     */
    List<DatabaseSpecialDto> all();

    /**
     * 根据ID删除
     * @param id
     */
    void delete(String id);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    DatabaseSpecialDto getDotById(String id);

    /**
     * 更新
     * @param databaseSpecialDto
     * @return
     */
    DatabaseSpecialDto saveDto(DatabaseSpecialDto databaseSpecialDto);

    /**
     * 数据库授权
     * @param databaseDto
     */
    void empowerDatabaseSperial(DatabaseDto databaseDto);

    /**
     * 单独资料授权
     * @param databaseSpecialReadWriteDto
     */
    void empowerDataOne(DatabaseSpecialReadWriteDto databaseSpecialReadWriteDto);

    /**
     * 批量资料授权
     * @param databaseSpecialReadWriteDtoList
     */
    void empowerDataBatch(List<DatabaseSpecialReadWriteDto> databaseSpecialReadWriteDtoList);
}
