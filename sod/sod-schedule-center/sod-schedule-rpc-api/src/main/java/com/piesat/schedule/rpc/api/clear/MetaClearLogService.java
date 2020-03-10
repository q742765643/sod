package com.piesat.schedule.rpc.api.clear;

import com.piesat.schedule.rpc.dto.clear.MetaClearLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-03-09 17:36
 **/
public interface MetaClearLogService {
    public PageBean selectMetaClearLogList(PageForm<MetaClearLogDto> pageForm);

    public MetaClearLogDto findMetaClearLogById(String metaClearLogId);

    public void deleteMetaClearLogByIds(String[] metaClearLogIds);

    public void exportExcel(MetaClearLogDto metaClearLogDto);
}

