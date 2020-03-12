package com.piesat.schedule.rpc.api.clear;

import com.piesat.schedule.rpc.dto.clear.ClearDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2019-12-24 11:25
 **/
public interface ClearService {
    public PageBean selectClearList(PageForm<ClearDto> pageForm);

    public ClearDto selectClearByParam(String databaseId, String dataClassId);

    public ClearDto findClearById(String clearId);
    public void saveClear(ClearDto clearDto);
    public void updateClear(ClearDto clearDto);
    public void deleteClearByIds(String[] clearIds);
    public List<Map<String,Object>> findDatabase();
    public List<Map<String,Object>> findDataClassId(String dataBaseId,String dataClassId);

    public void exportExcel(ClearDto clearDto);
}

