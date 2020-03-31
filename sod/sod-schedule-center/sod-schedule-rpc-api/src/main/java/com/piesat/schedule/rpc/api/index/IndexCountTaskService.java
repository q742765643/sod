package com.piesat.schedule.rpc.api.index;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;

/** 首页定时统计
*@description
*@author wlg
*@date 2020年3月30日下午3:57:17
*
*/
@GrpcHthtService(server = "indexCountTaskService",serialization = SerializeType.PROTOSTUFF)
public interface IndexCountTaskService {
	/**
	 *  运行资料数量统计
	 * @description 
	 * @author wlg
	 * @date 2020年3月30日下午5:05:59
	 */
	void runClassCountTask();

	
}
