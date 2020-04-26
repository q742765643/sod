package com.piesat.dm.rpc.api.dataclass;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.dataclass.LogicDefineDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * 数据用途定义
 *
 * @author cwh
 * @date 2019年 11月22日 15:35:27
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface LogicDefineService {

    public PageBean selectPageList(PageForm<LogicDefineDto> pageForm);

    List<LogicDefineDto> getAllLogicDefine();

    List<LogicDefineDto> findByLogicFlag(String logicFlag);

    LogicDefineDto saveDto(LogicDefineDto logicDefineDto);

    LogicDefineDto getDotById(String id);

    void delete(String id);

    List<LogicDefineDto> all();

    List<LogicDefineDto> findByParam(LogicDefineDto logicDefineDto);

    LogicDefineDto updateDto(LogicDefineDto logicDefineDto);

    List<Map<String,Object>> getLogicByUserId(String databaseIds);

    void exportExcel(LogicDefineDto logicDefineDto);
}
