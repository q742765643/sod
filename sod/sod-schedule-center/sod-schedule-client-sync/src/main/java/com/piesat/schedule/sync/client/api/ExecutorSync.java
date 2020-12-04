package com.piesat.schedule.sync.client.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:30
 */
@GrpcHthtService(server = GrpcConstant.SCHEDULE_CLIENT_SYNC,serialization = SerializeType.PROTOSTUFF)
public interface ExecutorSync {
    public void execute(JobInfoEntity jobInfo);
}
