package com.piesat.sod.system.rpc.api;

import java.util.Map;

import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.common.jpa.page.PageBean;
import com.piesat.common.jpa.page.PageForm;

/** 
*@description
*@author wlg
*@date 2019年11月20日上午11:47:04
*
*/
@GrpcHthtService(server = "dbfile",serialization = SerializeType.PROTOSTUFF)
public interface DbFileService {

	/**
	 *  获取表分页数据
	 * @description 
	 * @author wlg
	 * @date 2019年11月20日下午1:50:45
	 * @param params
	 * @param pageForm
	 * @return
	 */
	PageBean findPageData(Map<String,Object> params,PageForm pageForm) throws Exception;
	
	/**
	 * 上传文件
	 * @description 
	 * @author wlg
	 * @date 2019年11月21日下午5:01:04
	 * @param request
	 * @throws Exeption
	 */
	void save(MultipartHttpServletRequest request) throws Exception;
}
