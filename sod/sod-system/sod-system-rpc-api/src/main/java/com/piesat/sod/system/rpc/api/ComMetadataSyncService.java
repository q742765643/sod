package com.piesat.sod.system.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;

/** 公共元数据同步配置
*@description
*@author wlg
*@date 2020年2月6日下午5:08:57
*
*/
@GrpcHthtService(server = "comMetadata",serialization = SerializeType.PROTOSTUFF)
public interface ComMetadataSyncService {

}
