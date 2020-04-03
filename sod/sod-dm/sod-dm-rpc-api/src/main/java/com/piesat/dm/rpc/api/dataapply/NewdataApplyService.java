package com.piesat.dm.rpc.api.dataapply;

import com.piesat.dm.rpc.dto.dataclass.DataClassDto;
import com.piesat.dm.rpc.dto.dataclass.DataLogicDto;
import com.piesat.dm.rpc.dto.datatable.DataTableDto;
import com.piesat.dm.rpc.dto.datatable.TableColumnDto;
import com.piesat.dm.rpc.dto.dataapply.NewdataApplyDto;
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

    public PageBean selectPageList(PageForm<Map<String,Object>> pageForm);

    public NewdataApplyDto updateStatus(NewdataApplyDto newdataApplyDto);

    List<Map<String,Object>> getLogicInfo();

    Map<String,Object> queryCheckByApplyId(String id);

    ResultT<String> addGroup(DataClassDto dataClassDto, NewdataApplyDto newdataApplyDto);

    ResultT<String> updateGroup(DataClassDto dataClassDto, NewdataApplyDto newdataApplyDto ,String old_data_class_id);

    ResultT<String> addOrUpdateDataTable(DataTableDto dataTableDto);

    List<DataTableDto> getDataTableByType(DataLogicDto dataLogicDto);

    List<TableColumnDto> getNewdataTableField(String id);

    ResultT<String> addDataStructure(TableColumnDto tableColumnDto);

    ResultT<String> updateDataStructure(TableColumnDto tableColumnDto);

    PageBean getTableDataInfo(String tableId, String databaseId, int pageNum, int pageSize);

    Map<String,Object> getArchiveInfo(String id);

    List<Map<String,Object>> getByUserIdAndApplyId(NewdataApplyDto newdataApplyDto);

    List<Map<String,Object>> getColumnByIdAndDDataId(NewdataApplyDto newdataApplyDto);

    void deleteById(String id);

}
