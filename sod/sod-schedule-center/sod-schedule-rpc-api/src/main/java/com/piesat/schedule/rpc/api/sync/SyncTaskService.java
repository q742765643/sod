package com.piesat.schedule.rpc.api.sync;

import com.alibaba.fastjson.JSONObject;
import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.schedule.rpc.dto.move.MoveDto;
import com.piesat.schedule.rpc.dto.sync.SyncTaskDto;
import com.piesat.schedule.rpc.dto.sync.SyncTaskLogDto;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

import java.util.List;

/**
 * @author yaya
 * @description TODO
 * @date 2020/1/13 17:01
 */
@GrpcHthtService(server = GrpcConstant.SCHEDULE_SERVER,serialization = SerializeType.PROTOSTUFF)
public interface SyncTaskService {
    public PageBean selectPageList(PageForm<SyncTaskDto> pageForm);

    public PageBean selectLogPageList(PageForm<SyncTaskLogDto> pageForm);

    public List<SyncTaskDto> all();

    public SyncTaskDto saveDto(SyncTaskDto syncTaskDto);

    public SyncTaskDto updateDto(SyncTaskDto syncTaskDto);

    public SyncTaskDto getDtoById(String id);

    public void deleteSync(String taskId);

    public JSONObject getSyncJsonById(String taskId);

    void exportExcel(SyncTaskDto syncTaskDto);

    void restart(String taskId);

    void stop(String taskId);
}
