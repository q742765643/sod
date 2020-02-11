package com.piesat.dm.rpc.api.newdata;

import com.piesat.dm.rpc.dto.DataClassDto;
import com.piesat.dm.rpc.dto.DataLogicDto;
import com.piesat.dm.rpc.dto.DataTableDto;
import com.piesat.dm.rpc.dto.TableColumnDto;
import com.piesat.dm.rpc.dto.newdata.NewdataApplyDto;
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

    public PageBean selectPageList(NewdataApplyDto newdataApplyDto, int pageNum, int pageSize);

    public int updateStatus(NewdataApplyDto newdataApplyDto);

    List<Map<String,Object>> getLogicInfo();

    Map<String,Object> queryCheckByApplyId(NewdataApplyDto newdataApplyDto);

    ResultT<String> addGroup(DataClassDto dataClassDto, NewdataApplyDto newdataApplyDto);

    ResultT<String> updateGroup(DataClassDto dataClassDto, NewdataApplyDto newdataApplyDto ,String old_data_class_id);

    ResultT<String> addOrUpdateDataTable(DataTableDto dataTableDto);

    List<DataTableDto> getDataTableByType(DataLogicDto dataLogicDto);

    List<TableColumnDto> getNewdataTableField(String id);

    ResultT<String> addDataStructure(TableColumnDto tableColumnDto);

    ResultT<String> updateDataStructure(TableColumnDto tableColumnDto);

    PageBean getTableDataInfo(String tableId, String databaseId, int pageNum, int pageSize);

    Map<String,Object> getArchiveInfo(String id);

}
