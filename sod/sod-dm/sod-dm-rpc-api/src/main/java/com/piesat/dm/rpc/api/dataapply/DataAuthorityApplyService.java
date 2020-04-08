package com.piesat.dm.rpc.api.dataapply;

import com.piesat.dm.rpc.dto.dataapply.DataAuthorityApplyDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/10 13:00
 */
public interface DataAuthorityApplyService {

    PageBean selectPageList(PageForm<DataAuthorityApplyDto> pageForm);

    DataAuthorityApplyDto saveDto(DataAuthorityApplyDto dataAuthorityApplyDto);

    Map<String,Object> getObjectById(String id);

    List<Map<String,Object>> getRecordByApplyId(Map<String,String> map);

    Map<String,Object> updateRecordCheck(DataAuthorityApplyDto dataAuthorityApplyDto);

    Map<String,Object>  updateRecordCheckCancel(DataAuthorityApplyDto dataAuthorityApplyDto);

    List<Map<String,Object>> getRecordListByUserId(String userId);

    void updateRecordByApplyIdAndClassId(String apply_id,String data_class_id,Integer authorize, String cause);

    void updateRecordByApplyIdAndClassIdAndDatabaseId(String apply_id,String data_class_id,String database_id,Integer authorize, String cause);
}
