package com.piesat.sod.system.rpc.api;

import com.piesat.sod.system.rpc.dto.GribParameterDefineDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/3/9 17:51
 */
public interface GribParameterDefineService {

    PageBean selectPageList(PageForm<GribParameterDefineDto> pageForm);

    GribParameterDefineDto saveDto(GribParameterDefineDto ribParameterDefineDto);

    GribParameterDefineDto updateDto(GribParameterDefineDto ribParameterDefineDto);

    public void deleteRecordByIds(List<String> ids);

    List<GribParameterDefineDto> all();

    void exportExcel(GribParameterDefineDto gribParameterDefineDto);

    List<GribParameterDefineDto> findByParam(GribParameterDefineDto gribParameterDefineDto);
}
