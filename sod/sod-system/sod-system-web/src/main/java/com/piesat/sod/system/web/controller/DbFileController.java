package com.piesat.sod.system.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.piesat.common.jpa.page.PageBean;
import com.piesat.common.jpa.page.PageForm;
import com.piesat.sod.system.rpc.api.DbFileService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/** 数据库文档 控制器
*@description
*@author wlg
*@date 2019年11月20日下午2:30:17
*
*/
@RestController
@Api(value="数据库文档Controller",tags = {"数据库文档操作接口"})
public class DbFileController {
	
	
	@Autowired
	private DbFileService dbFileService;
	
	/**
	 * 获取数据库文件分页数据接口
	 * @description 
	 * @author wlg
	 * @date 2019年11月20日下午3:25:52
	 * @param request
	 * @param pageForm
	 * @return
	 */
	@ApiOperation(value="获取数据库文件分页数据接口",notes="获取数据库文件分页数据接口")
	@GetMapping(value="/api/dbfile/getpage")
	public PageBean getPageData(HttpServletRequest request,PageForm pageForm) {
		try {
			Map<String,Object> param = new HashMap<>();
			param.put("fileType", request.getParameter("fileType"));
			param.put("fileName", request.getParameter("fileName"));
			param.put("fileSuffix", request.getParameter("fileSuffix"));
			param.put("fileStartDate", request.getParameter("fileStartDate"));
			param.put("fileEndDate", request.getParameter("fileEndDate"));
			param.put("field", request.getParameter("field"));
			param.put("order", request.getParameter("order"));
			return dbFileService.findPageData(param, pageForm);
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 *  上传文件
	 * @description 
	 * @author wlg
	 * @date 2019年11月21日上午11:12:41
	 * @param request
	 * @return
	 */
	@ApiOperation(value="数据库文档上传文件接口",notes="数据库文档上传文件接口")
	@PostMapping(value="/api/dbfile/upload")
	public Map<String,Object> uploadFile(MultipartHttpServletRequest request) {
		Map<String,Object> result = new HashMap<>();
		try {
			dbFileService.save(request);
			result.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("success", false);
			result.put("msg", e.getMessage());
		}
		return result;
	}

}
