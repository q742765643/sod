package com.piesat.dm.rpc.api.dataapply;

import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
import com.piesat.dm.rpc.dto.dataclass.DataClassLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableInfoDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.util.ResultT;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @author yaya
 * @description TODO
 * @date 2020/2/5 18:59
 */
public interface NewdataApplyService {

    public PageBean selectPageList(PageForm<Map<String, Object>> pageForm);

    NewdataApplyDto getDotById(String id);

    NewdataApplyDto saveDto(NewdataApplyDto newdataApplyDto);

    NewdataApplyDto updateDto(NewdataApplyDto newdataApplyDto);

    public NewdataApplyDto updateStatus(NewdataApplyDto newdataApplyDto);

    List<Map<String, Object>> getLogicInfo();

    Map<String, Object> queryCheckByApplyId(String id);



    List<DataTableInfoDto> getDataTableByType(DataClassLogicDto dataLogicDto);

    ResultT<String> addDataStructure(TableColumnDto tableColumnDto);

    ResultT<String> updateDataStructure(TableColumnDto tableColumnDto);

    PageBean getTableDataInfo(String tableId, String databaseId, int pageNum, int pageSize);

    Map<String, Object> getArchiveInfo(String id);

    List<Map<String, Object>> getByUserIdAndApplyId(NewdataApplyDto newdataApplyDto);

    List<Map<String, Object>> getColumnByIdAndDDataId(NewdataApplyDto newdataApplyDto);

    void deleteById(String id);

    void delApply(String applyId, String dDataId);

    Map<String, Object> getSchemaInfo(String physicId, String userId);

    List<Map<String, Object>> getDataInfoByUserId(String userId, String dataClassId);

    List<Map<String, Object>> getSpecialDBData(String sdbId);

    List<NewdataApplyDto> findByDataClassIdAndUserId(String dataClassId, String userId);

    List<NewdataApplyDto> findByUserId(String userId);
}
