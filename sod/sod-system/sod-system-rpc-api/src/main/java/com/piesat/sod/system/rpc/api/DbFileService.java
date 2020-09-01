package com.piesat.sod.system.rpc.api;

import com.piesat.common.grpc.annotation.GrpcHthtService;
import com.piesat.common.grpc.constant.SerializeType;
import com.piesat.sod.system.rpc.dto.DbFileDto;
import com.piesat.util.ResultT;
import com.piesat.util.constant.GrpcConstant;
import com.piesat.util.page.PageBean;
import com.piesat.util.page.PageForm;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/** 数据库文档管理
*@description
*@author wlg
*@date 2019年11月20日上午11:47:04
*
*/
@GrpcHthtService(server = GrpcConstant.SYSTEM_SERVER,serialization = SerializeType.PROTOSTUFF)
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
	PageBean findPageData(PageForm<DbFileDto> pageForm) throws Exception;

	/**
	 * 上传文件
	 * @description
	 * @author wlg
	 * @date 2019年11月21日下午5:01:04
	 * @param request
	 * @throws Exeption
	 */
	ResultT save(MultipartHttpServletRequest request) throws Exception;
	/**
	 *  根据id批量删除记录
	 * @description
	 * @author wlg
	 * @date 2019年12月23日上午8:12:10
	 * @param recordIds
	 * @throws Exception
	 */
	void deleteByIds(String recordIds) throws Exception;

	void downloadFile(HttpServletResponse response, String name);

	DbFileDto fileSave(MultipartFile multipartFile,MultipartHttpServletRequest request);
}
