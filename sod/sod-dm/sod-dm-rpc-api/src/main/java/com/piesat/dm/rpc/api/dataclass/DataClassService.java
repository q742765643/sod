package com.piesat.dm.rpc.api.dataclass;

import com.alibaba.fastjson.JSONArray;
import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.entity.dataclass.DataClassEntity;
import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.apache.ibatis.annotations.Param;

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

    List<DataClassDto> findByDDataId(String dDataId);

    JSONArray getDatabaseClass();

    JSONArray getDatabaseClassPostgresql();

    JSONArray getDatabaseClassMysql();

    JSONArray getTree();

    List<Map<String, Object>> getListBYIn(List<String> classIds, String className, String dDataId);

    List<Map<String, Object>> getDataTypeList();

    /**
     * 获取所有目录
     *
     * @return
     */
    List<Map<String, Object>> getDataGroup();

    /**
     * 获取data_class_id最大编码
     *
     * @return
     */
    public String getDataClassIdNum();

    PageBean getBaseData(PageForm<Map<String, String>> pageForm, DataClassDto dataClassDto);


    List<DataClassDto> findAllCategory();

    void exportBaseData(DataClassDto dataClassDto);


    String findByParentId(String parentId);

    List<Map<String, Object>> getLogicByDdataId(String dDataId);

    JSONArray getSimpleTree(String databaseId);

    Map<String, Object> getDataClassCoreInfo(String c_datum_code);

    DataClassDto updateIsAllLine(DataClassDto dataClassDto);

    Map<String, Object> getArchive(String ddataid);

    /**
     * 是否存在资料
     *
     * @param dDataId
     * @return
     */
    ResultT haveClassByDataId(String dataId);

    List<DataClassDto> findByDataClassIdAndCreateBy(String dataclassId, String userId);
}
