package com.piesat.dm.rpc.api;

import com.alibaba.fastjson.JSONArray;
import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
import java.util.Map;

/**
 * 资料分类
 *
 * @author cwh
 * @date 2019年 11月22日 15:33:10
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataClassService {
    DataClassDto saveDto(DataClassDto dataClassDto);

    DataClassDto getDotById(String id);

    void delete(String id);

    void deleteByDataClassId(String dataClassId);

    List<DataClassDto> all();

    JSONArray getLogicClass();

    DataClassDto findByDataClassId(String dataClassId);

    JSONArray getDatabaseClass();

    JSONArray getTree();

    List<Map<String, Object>> getListBYIn(List<String> classIds, String className, String dDataId);

    public List<Map<String, Object>> getDataTypeList();

    /**
     * 获取所有目录
     * @return
     */
    public List<Map<String, Object>>  getDataGroup();
}
