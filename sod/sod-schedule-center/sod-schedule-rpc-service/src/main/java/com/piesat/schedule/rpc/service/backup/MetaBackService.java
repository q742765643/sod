package com.piesat.schedule.rpc.service.backup;

import com.piesat.common.grpc.annotation.GrpcHthtClient;
import com.piesat.schedule.client.api.ExecutorBiz;
import com.piesat.schedule.client.api.vo.TreeVo;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: sod
 * @description:
 * @author: zzj
 * @create: 2020-02-24 16:04
 **/
@Service
public class MetaBackService {
    @GrpcHthtClient
    private ExecutorBiz executorBiz;

    public List<TreeVo> findMeta(String parentId, String databaseType){
        return executorBiz.findMeta(parentId,databaseType);
    }

}

