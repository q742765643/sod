package com.piesat.dm.rpc.api.database;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.database.DatabaseDto;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;

import java.util.List;

/**
 * 数据库类型定义
 *
 * @author cwh
 * @date 2019年 11月22日 15:30:47
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DatabaseService {
    DatabaseDto saveDto(DatabaseDto databaseDto);

    DatabaseDto getDotById(String id);

    void delete(String id);

    ResultT delByIds(String ids);

    List<DatabaseDto> all();

    List<DatabaseDto> export(String id, String databaseName);

    List<DatabaseDto> findByType(String databaseType);

    PageBean getPage(DatabaseDto databaseDto, int pageNum, int pageSize);

    DatabaseDto conStatus(String id);

    ResultT connStatus(DatabaseDto databaseDto);

    List<DatabaseDto> findByIdIn(List<String> ids);

    void exportExcel(String id,String databaseName);

    List<DatabaseDto> getDatabaseDefineList();
}
