package com.piesat.schedule.rpc.api.move;

import com.piesat.schedule.rpc.dto.move.MoveLogDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/**
 * Created by zzj on 2019/12/29.
 */
public interface MoveLogService {
    public PageBean selectMoveLogList(PageForm<MoveLogDto> pageForm);

    public MoveLogDto selectMoveLoByJobId(String jobId);

    public MoveLogDto findMoveLogById(String moveLogId);

    public void deleteMoveLogByIds(String[] moveLogIds);

    public void exportExcel(MoveLogDto moveLogDto);
}
