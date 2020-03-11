package com.piesat.schedule.rpc.api.clear;

import com.piesat.schedule.rpc.dto.clear.ClearLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * Created by zzj on 2019/12/29.
 */
public interface ClearLogService {

    public PageBean selectClearLogList(PageForm<ClearLogDto> pageForm);

    public ClearLogDto selectClearLoByJobId(String jobId);

    public ClearLogDto findClearLogById(String clearLogId);

    public void deleteClearLogByIds(String[] clearLogIds);

    public void exportExcel(ClearLogDto clearLogDto);
}
