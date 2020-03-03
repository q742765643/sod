package com.piesat.schedule.client.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:30
 */
@GrpcHthtService(server = "schedule-client-server",serialization = SerializeType.PROTOSTUFF)
public interface ExecutorBiz {
    public void execute(JobInfoEntity jobInfo);

    public List<TreeVo> findMeta(String parentId, String databaseType);

    public void recover(MetaRecoverLogEntity recoverLogEntity);

    public  List<TreeVo> getFileList(String parentId,String storageDirectory);

    public List<TreeVo> getFileChidren(String childrenPath);

    public Map<String,Object> parsingPath(String path);

    public void handMetaBack(JobInfoEntity jobInfoEntity);

    public Object downFile(String path);
}
