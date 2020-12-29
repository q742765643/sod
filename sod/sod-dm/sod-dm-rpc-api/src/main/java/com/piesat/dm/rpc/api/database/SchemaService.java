package com.piesat.dm.rpc.api.database;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.database.SchemaDto;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
import java.util.Map;

/**
 * 数据库基础库专题库
 *
 * @author cwh
 * @date 2019年 11月22日 15:31:30
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface SchemaService {
    SchemaDto saveDto(SchemaDto schemaDto);

    SchemaDto getDotById(String id);

    void delete(String id);

    ResultT deleteById(String id);

    List<SchemaDto> all();

    List<Map<String,Object>> getDatabaseName();

    List<Map<String,Object>> getByDatabaseDefineId(String id);

    List<SchemaDto> findByLevel(int level);

    List<SchemaDto> findByDatabaseClassifyAndIdIn(String databaseClassify, List<String> ids);

    List<SchemaDto> findByDatabaseClassifyAndDatabaseDefineIdIn(String databaseClassify, List<String> databaseDefineIds);

    List<SchemaDto> findByDatabaseDefineIdIn(List<String> databaseDefineIds);

    List<SchemaDto> findByDatabaseDefineId(String id);

    List<SchemaDto> findByDatabaseClassify(String databaseClassify);

    List<Map<String,Object>> findByUserIdAndDatabaseDefineId(String userId,String databaseDefineId);

    List<Map<String, Object>> getDatabaseList(String ifDisplay);
}
