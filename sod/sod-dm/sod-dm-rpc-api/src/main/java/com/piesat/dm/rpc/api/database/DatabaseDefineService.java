package com.piesat.dm.rpc.api.database;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.database.DatabaseDefineDto;
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
public interface DatabaseDefineService {
    DatabaseDefineDto saveDto(DatabaseDefineDto databaseDefineDto);

    DatabaseDefineDto getDotById(String id);

    void delete(String id);

    void delByIds(String ids);

    List<DatabaseDefineDto> all();

    List<DatabaseDefineDto> export(String id, String databaseName);

    List<DatabaseDefineDto> findByType(String databaseType);

    PageBean getPage(DatabaseDefineDto databaseDefineDto,int pageNum,int pageSize);

    DatabaseDefineDto conStatus(String id);

    ResultT connStatus(DatabaseDefineDto databaseDefineDto);

    List<DatabaseDefineDto> findByIdIn(List<String> ids);

    void exportExcel(String id,String databaseName);

    List<DatabaseDefineDto> getDatabaseDefineList();
}
