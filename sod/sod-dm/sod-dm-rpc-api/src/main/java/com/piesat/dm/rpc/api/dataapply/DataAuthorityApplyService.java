package com.piesat.dm.rpc.api.dataapply;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.dm.rpc.dto.ReadAuthorityDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.dm.rpc.dto.dataapply.DataAuthorityRecordDto;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 13:00
 */
@GrpcHthtService(server = GrpcConstant.DM_SERVER, serialization = SerializeType.PROTOSTUFF)
public interface DataAuthorityApplyService {

    PageBean selectPageList(PageForm<DataAuthorityApplyDto> pageForm);

    DataAuthorityApplyDto saveDto(DataAuthorityApplyDto dataAuthorityApplyDto);

    Map<String,Object> getObjectById(String id);

    List<Map<String,Object>> getRecordByApplyId(Map<String,String> map);

    ResultT updateRecordCheck(DataAuthorityApplyDto dataAuthorityApplyDto);

    ResultT updateOneRecordCheck(String userId, DataAuthorityRecordDto dataAuthorityRecordDto);

    ResultT  updateRecordCheckCancel(DataAuthorityApplyDto dataAuthorityApplyDto);

    List<Map<String,Object>> getRecordListByUserId(String userId);

    void updateRecordByApplyIdAndClassId(String apply_id,String data_class_id,Integer authorize, String cause);

    Map<String,Object>  getAuthorDataByClassId(String dataClassId);

    void updateRecordByApplyIdAndClassIdAndDatabaseId(String apply_id,String data_class_id,String database_id,Integer authorize, String cause);

    Map<String, Object> getDataAuthorityList(String userId, String applyAuthority, String logicId, String dataName, String category, String schemaId);

    Map<String, Object> getDataCreator(String dataClassId);

    Map<String, Object> deleteDataAuthorityById(String applyId, String dataBaseId, String dataClassId);

    Map<String, Object> getDataCategory();
    /**
     *  获取可申请的资料清单
     * @description
     * @author wlg
     * @date 2020年4月22日下午4:18:02
     * @param userId
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> getApplyDataInfo(String userId) throws Exception;

    ReadAuthorityDto updateReadAuthority(ReadAuthorityDto readAuthorityDto);

    List<ReadAuthorityDto> getReadAuthority();

    List<DataAuthorityApplyDto> findByUserId(String userId);

    void deleteByDataClassId(String dataclassId);
}
