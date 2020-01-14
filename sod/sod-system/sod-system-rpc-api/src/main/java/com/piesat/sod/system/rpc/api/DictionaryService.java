package com.piesat.sod.system.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.sod.system.rpc.dto.DictionaryDto;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;

/** 字典表管理
*@description
*@author wlg
*@date 2020年1月14日下午4:57:46
*
*/
@GrpcHthtService(server = "dictionary",serialization = SerializeType.PROTOSTUFF)
public interface DictionaryService {
	/**
	 *  获取分页数据
	 * @description 
	 * @author wlg
	 * @date 2020年1月14日下午5:02:17
	 * @param pageForm
	 * @return
	 * @throws Exception
	 */
	PageBean findPageData(PageForm<DictionaryDto> pageForm) throws Exception;

}
