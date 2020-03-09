package com.piesat.schedule.client.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.schedule.client.api.vo.TreeVo;
import com.piesat.schedule.entity.JobInfoEntity;
import com.piesat.schedule.entity.recover.MetaRecoverLogEntity;
import com.piesat.schedule.entity.recover.RecoverLogEntity;
import com.piesat.util.constant.GrpcConstant;

import java.util.List;
import java.util.Map;

/**
 * @program: sod
 * @描述
 * @创建人 zzj
 * @创建时间 2019/12/30 18:30
 */
@GrpcHthtService(server = GrpcConstant.SCHEDULE_CLIENT_SERVER,serialization = SerializeType.PROTOSTUFF)
public interface ExecutorBiz {
    public void execute(JobInfoEntity jobInfo);

    public List<TreeVo> findMeta(String parentId, String databaseType);

    public List<TreeVo> findAllTableByIp(String parentId, String databaseType);

    public void recover(MetaRecoverLogEntity recoverLogEntity);

    public  List<TreeVo> getFileList(String parentId,String storageDirectory);

    public  List<TreeVo> getDataFileList(RecoverLogEntity recoverLogEntity);

    public List<TreeVo> getFileChidren(String childrenPath);

    public Map<String,Object> parsingPath(String path);

    public void handMetaBack(JobInfoEntity jobInfoEntity);

    public void recoverStructedData(RecoverLogEntity recoverLogEntity);

    public List<Map<String,Object>> md5Check(List<String> paths);

}
