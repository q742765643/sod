package com.piesat.schedule.client.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.util.constant.GrpcConstant;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:30
 */
@GrpcHthtService(server = "schedule-client-server",serialization = SerializeType.PROTOSTUFF)
public interface ExecutorBiz {
    public void execute(JobInfoEntity jobInfo);
}
